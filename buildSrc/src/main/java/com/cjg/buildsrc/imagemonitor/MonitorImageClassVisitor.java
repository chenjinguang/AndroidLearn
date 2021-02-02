package com.cjg.buildsrc.imagemonitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;


/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/2
 * @修改人和其它信息
 */
public class MonitorImageClassVisitor extends ClassVisitor {

    public MonitorImageClassVisitor( ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }


    /**
     * 访问类会进入这里
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if(superName.equals("android/widget/ImageView")
                && !name.equals("com/cjg/asmdemo/MonitorImageView")){
            superName = "com/cjg/asmdemo/MonitorImageView";
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
