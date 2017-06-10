package com.cloudcommander.vendor.module.sorter;

import com.cloudcommander.vendor.module.modules.DefaultModuleImpl;
import com.cloudcommander.vendor.module.modules.Module;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModuleSorterImplTest {

    private final ModuleSorter moduleSorter = new DefaultModuleSorterImpl();

    @Test
    public void testEmpty(){
        //Test
        List<Module> modules = moduleSorter.sort(Collections.emptyList());

        //Verify
        assertEquals(0, modules.size());
    }

    @Test
    public void testOne(){
        //Prepare
        Module module = new DefaultModuleImpl("mod1", Collections.emptyList());

        //Test
        List<Module> modules = moduleSorter.sort(Collections.singletonList(module));

        //Verify
        assertEquals(1, modules.size());
        assertEquals(module, modules.get(0));
    }

    @Test
    public void testSimpleDependencies(){
        //Prepare
        Module module1 = new DefaultModuleImpl("module1", Collections.emptyList());
        Module module2 = new DefaultModuleImpl("module2", Collections.singletonList("module1"));
        Module module3 = new DefaultModuleImpl("module3", Collections.singletonList("module2"));

        //Test
        List<Module> modules = moduleSorter.sort(Arrays.asList(module1, module2, module3));

        //Verify
        assertEquals(3, modules.size());
        assertEquals(module1, modules.get(0));
        assertEquals(module2, modules.get(1));
        assertEquals(module3, modules.get(2));
    }

    @Test
    public void testSimpleDependenciesUnsorted(){
        //Prepare
        Module module1 = new DefaultModuleImpl("module1", Collections.emptyList());
        Module module2 = new DefaultModuleImpl("module2", Collections.singletonList("module1"));
        Module module3 = new DefaultModuleImpl("module3", Collections.singletonList("module2"));

        //Test
        List<Module> modules = moduleSorter.sort(Arrays.asList(module2, module1, module3));

        //Verify
        assertEquals(3, modules.size());
        assertEquals(module1, modules.get(0));
        assertEquals(module2, modules.get(1));
        assertEquals(module3, modules.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCircularDependencies(){
        //Prepare
        Module module1 = new DefaultModuleImpl("module1", Collections.singletonList("module3"));
        Module module2 = new DefaultModuleImpl("module2", Collections.singletonList("module1"));
        Module module3 = new DefaultModuleImpl("module3", Collections.singletonList("module2"));

        //Test
        moduleSorter.sort(Arrays.asList(module2, module1, module3));

        //Verify
        fail(); //Should never run
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatedModuleName(){
        //Prepare
        Module module1a = new DefaultModuleImpl("module1", Collections.emptyList());
        Module module1b = new DefaultModuleImpl("module1", Collections.emptyList());

        //Verify
        moduleSorter.sort(Arrays.asList(module1a, module1b)); //Throw exception!

        //Verify
        fail(); //Should never run
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnresolvedDependencies(){
        //Prepare
        Module module1 = new DefaultModuleImpl("module1", Collections.singletonList("moduleasd"));
        Module module2 = new DefaultModuleImpl("module2", Collections.emptyList());

        //Verify
        moduleSorter.sort(Arrays.asList(module1, module2)); //Throw exception!

        //Verify
        fail(); //Should never run
    }
}
