package com.cloudcommander.vendor.module.sorter;

import com.cloudcommander.vendor.module.modules.Module;

import java.util.Collection;
import java.util.List;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public interface ModuleSorter {

    List<Module> sort(final Collection<Module> modules);

}
