package com.cloudcommander.gradle.plugins.module

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModulePlugin implements Plugin<Project> {
    private boolean wasApplied = false;

    void apply(final Project project) {
        if(wasApplied){
            project.logger.warn('The com.cloudcommander.module was already applied to the project: ' + project.path)
        }else{
            doApply(project);
        }
    }

    private void doApply(final Project project){
        project.apply plugin: 'java'
        project.apply plugin: 'idea'
        project.apply plugin: 'eclipse'
        project.apply plugin: 'org.springframework.boot'

        project.task("generate-cc-module", type: CCModuleTask) {
            group = "CloudCommanderModulePlugin"
            description = "Generate Cloud Commander module"
        }

        project.tasks.compileJava.dependsOn("generate-cc-module")

        project.sourceSets.main.java.srcDir(project.tasks['generate-cc-module'].javaDir)
        project.sourceSets.main.resources.srcDir(project.tasks['generate-cc-module'].resourcesDir)

        project.tasks.bootRepackage.enabled = false;

        project.dependencies.add("compile", "org.springframework.boot:spring-boot-starter")
    }
}
