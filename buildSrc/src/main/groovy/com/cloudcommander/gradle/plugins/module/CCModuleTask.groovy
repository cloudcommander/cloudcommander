package com.cloudcommander.gradle.plugins.module;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import org.apache.commons.text.WordUtils;

class CCModuleTask extends DefaultTask {

    File baseDir = new File(project.buildDir, "generated/source/cloud-commander/module/")

    @OutputDirectory
    File resourcesDir = new File(baseDir, "resources")

    @OutputDirectory
    File javaDir = new File(baseDir, "java")

    @TaskAction
    def action() {
        resourcesDir.mkdirs();
        javaDir.mkdirs();

        generateModuleFile();
        generateConfigurationFile();
        generateModuleServicesFile();
    }

    private void generateModuleFile(){
        MethodSpec constrMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement('super($S, $T.emptyList(), $T.class)', project.name, Collections.class, ClassName.get(project.group, getConfigurationClassName()))
                .build();

        String className = getClassPrefix() + "ModuleImpl";
        TypeSpec classSpec = TypeSpec.classBuilder(className)
                .superclass(ClassName.get("com.cloudcommander.vendor.module.modules", 'DefaultModuleImpl'))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constrMethodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(project.group + ".modules", classSpec)
                .build();

        javaFile.writeTo(javaDir);
    }

    private void generateConfigurationFile(){
        TypeSpec configurationTypeSpec = getConfigurationTypeSpec();
        JavaFile javaFile = JavaFile.builder(project.group, configurationTypeSpec)
                .build();

        javaFile.writeTo(javaDir);
    }

    private String getConfigurationClassName(){
        return getClassPrefix() + "Configuration";
    }

    private TypeSpec getConfigurationTypeSpec(){
        ClassName importAnnotationClassName = ClassName.get("org.springframework.context.annotation", 'ImportResource');
        AnnotationSpec resourceAnnotationSpec = AnnotationSpec.builder(importAnnotationClassName).addMember("value", '"classpath:'+ project.name +'/spring/'+ project.name +'-spring-config.xml"').build();

        String className = getConfigurationClassName();
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.context.annotation", 'Configuration'))
                .addAnnotation(resourceAnnotationSpec)
                .build();
    }

    private void generateModuleServicesFile(){
        File baseDir = new File(resourcesDir, "META-INF/services");
        baseDir.mkdirs();

        File servicesFile = new File(baseDir, "com.cloudcommander.vendor.module.modules.Module");

        PrintWriter printWriter = null;
        try{
            printWriter = new PrintWriter(servicesFile)
            printWriter.append(project.group + ".modules." + getClassPrefix() +"ModuleImpl")
            printWriter.flush()
            printWriter.close()
        }catch(Exception e){
            if(printWriter != null){
                printWriter.close()
            }

            throw e;
        }
    }

    private String getClassPrefix(){
        return WordUtils.capitalizeFully(project.name)
    }
}