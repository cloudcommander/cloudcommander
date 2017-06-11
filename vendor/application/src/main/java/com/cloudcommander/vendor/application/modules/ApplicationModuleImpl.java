package com.cloudcommander.vendor.application.modules;

import com.cloudcommander.vendor.application.ApplicationConfiguration;
import com.cloudcommander.vendor.module.modules.DefaultModuleImpl;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Adrian Tello on 11/06/2017.
 */
public class ApplicationModuleImpl extends DefaultModuleImpl{

    private final static String MODULE_NAME = "application";

    private final static Collection<String> REQUIRED_MODULES = Arrays.asList("module");

    public ApplicationModuleImpl() {
        super(MODULE_NAME, REQUIRED_MODULES, ApplicationConfiguration.class);
    }
}
