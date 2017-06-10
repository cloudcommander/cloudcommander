package com.cloudcommander.vendor.module.modules;

import java.util.Collection;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public interface Module {
    String getName();

    Collection<String> getRequiredModuleNames();
}
