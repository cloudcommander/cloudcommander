package com.cloudcommander.module.plugins

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
        project.apply plugin: 'org.springframework.boot'

        project.extensions.add("com.cloudcommander.module", ModulePluginExtension)

        project.task("generate-cc-module", type: CCModuleTask) {
            group = "CloudCommanderModulePlugin"
            description = "Generate Cloud Commander module"
        }

        project.tasks.compileJava.dependsOn("generate-cc-module")

        project.tasks.bootRepackage.enabled = false;

        project.dependencies.add("compile", "org.springframework.boot:spring-boot-starter")
    }
}