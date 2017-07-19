package com.cloudcommander.vendor.module.printer;

import com.cloudcommander.vendor.module.modules.Module;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public interface ModulePrinter {

    public void print(final List<Module> moduleList, final PrintStream outputStream);
}
