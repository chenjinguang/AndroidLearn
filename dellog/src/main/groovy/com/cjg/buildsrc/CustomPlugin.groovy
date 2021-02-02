package com.cjg.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.task('showCustomPluginBuildSrc'){
            doLast {
                println("InBuildSrc:Module Name is ${project.name}")
            }
        }
    }

}