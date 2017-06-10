package com.cloudcommander.vendor.module.modules;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class ModuleModuleImpl extends DefaultModuleImpl{

    private final static String MODULE_NAME = "module";

    public ModuleModuleImpl() {
        super(MODULE_NAME, Collections.emptyList());
    }
}
