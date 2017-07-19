package com.cloudcommander.module.plugins;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class CCModuleTask extends DefaultTask {

    @OutputDirectory File outputDir = new File(project.buildDir, "generated/source/cloud-commander/module/")
    File resourcesDir = new File(outputDir, "resources")
    File javaDir = new File(outputDir, "java")

    @TaskAction
    def action() {
        resourcesDir.mkdirs();
        javaDir.mkdirs();
    }
}