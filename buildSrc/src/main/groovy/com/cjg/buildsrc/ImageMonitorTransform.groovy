package com.cjg.buildsrc

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cjg.buildsrc.imagemonitor.MonitorImageClassVisitor
import org.objectweb.asm.ClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class ImageMonitorTransform extends Transform {

    public static final String NAME = "ImageMonitor"

    @Override
    String getName() {
        return NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->

                //do your work here,modify classes in the dirInput.
                handleDirectoryInput(dirInput,outputProvider)

            }
            input.jarInputs.each { JarInput jarInput ->
                //Do your work here,modify the jarInput.
                handleJarInputs(jarInput,outputProvider)

            }

        }

    }

    void handleDirectoryInput(DirectoryInput directoryInput,TransformOutputProvider outputProvider){
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                String name = file.name
                if (filterClass(name)) {
                    // 用来读 class 信息
                    ClassReader classReader = new ClassReader(file.bytes)
                    // 用来写
                    ClassWriter classWriter = new ClassWriter(0 /* flags */)
                    // 大家以后有需求，或者修改，基本只要改这里就可以了，asm 的常用操作
                    ClassVisitor classVisitor = new MonitorImageClassVisitor(classWriter)
                    // 下面还可以包多层
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    // 重新覆盖写入文件
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        // 把修改好的数据，写入到 output
        def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes,
                directoryInput.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    void handleJarInputs(JarInput jarInput,TransformOutputProvider outputProvider){
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            // 重名名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                //插桩class
                if (filterClass(entryName)) {
                    //class文件处理
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(0)
                    // 大家以后有需求，或者修改，基本只要改这里就可以了，asm 的常用操作
                    ClassVisitor classVisitor = new MonitorImageClassVisitor(classWriter)
                    // 下面还可以包多层
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    boolean filterClass(String className) {
        return (className.endsWith(".class") && !className.startsWith("R\$")
                && "R.class" != className && "BuildConfig.class" != className)
    }
}