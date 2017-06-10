package com.cloudcommander.vendor.module.printer;

import com.cloudcommander.vendor.module.modules.DefaultModuleImpl;
import com.cloudcommander.vendor.module.modules.Module;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModulePrinterImplTest {

    private final ModulePrinter modulePrinter = new DefaultModulePrinterImpl();

    private ByteArrayOutputStream byteArrayOutputStream;

    private PrintStream printStream;

    @Before
    public void setUp(){
        byteArrayOutputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(byteArrayOutputStream);
    }

    @Test
    public void testEmptyList(){
        //Test
        modulePrinter.print(Collections.emptyList(), printStream);

        //Verify
        assertEquals("", byteArrayOutputStream.toString());
    }

    @Test
    public void testSingleWoDeps(){
        //Prepare
        Module module = new DefaultModuleImpl("modname", Collections.emptyList());

        //Test
        modulePrinter.print(Collections.singletonList(module), printStream);

        //Verify
        assertEquals("Module [modname]. Dependencies: []\r\n", byteArrayOutputStream.toString());
    }

    @Test
    public void testSingleDeps(){
        //Prepare
        Module module = new DefaultModuleImpl("modname", Arrays.asList("dep1", "core"));

        //Test
        modulePrinter.print(Collections.singletonList(module), printStream);

        //Verify
        assertEquals("Module [modname]. Dependencies: [dep1, core]\r\n", byteArrayOutputStream.toString());
    }

    @Test
    public void testMultiple(){
        //Prepare
        Module module1 = new DefaultModuleImpl("modname1", Arrays.asList("dep1", "core"));
        Module module2 = new DefaultModuleImpl("modname2", Arrays.asList("modname1", "modname3"));
        Module module3 = new DefaultModuleImpl("modname3", Collections.emptyList());

        //Test
        modulePrinter.print(Arrays.asList(module1, module2, module3), printStream);

        //Verify
        assertEquals("Module [modname1]. Dependencies: [dep1, core]\r\n" +
                "Module [modname2]. Dependencies: [modname1, modname3]\r\n" +
                "Module [modname3]. Dependencies: []\r\n", byteArrayOutputStream.toString());
    }
}
