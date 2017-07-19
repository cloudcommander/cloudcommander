package com.cloudcommander.vendor.module.printer;

import com.cloudcommander.vendor.module.modules.Module;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModulePrinterImpl implements ModulePrinter{

    private final static String DEPENDENCIES_SEPARATOR = ", ";

    @Override
    public void print(final List<Module> moduleList, final PrintStream outputStream) {
        for(Module module: moduleList){
            final String moduleName = module.getName();
            final String dependenciesStr = getDependenciesStr(module.getRequiredModuleNames());

            outputStream.println("Module [" + moduleName +"]. Dependencies: ["+ dependenciesStr +"]");
        }
    }

    protected String getDependenciesStr(final Collection<String> dependencies){
        StringBuilder stringBuilder = new StringBuilder();

        for(String dependencyName: dependencies){
            if(stringBuilder.length() != 0){
                stringBuilder.append(DEPENDENCIES_SEPARATOR);
            }

            stringBuilder.append(dependencyName);
        }

        return stringBuilder.toString();
    }
}
