package com.cloudcommander.vendor.module.validators;

import com.cloudcommander.vendor.module.modules.DefaultModuleImpl;
import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModuleValidatorImplTest {

    private ModuleValidator moduleValidator = new DefaultModuleValidatorImpl();

    @Test
    public void testValid() throws ValidationFailedException {
        //Prepare
        Module module = new DefaultModuleImpl("module", Collections.emptyList());

        //Verify
        moduleValidator.validate(module);
    }

    @Test(expected = ValidationFailedException.class)
    public void testEmptyName() throws ValidationFailedException {
        //Prepare
        Module module = new DefaultModuleImpl("", Collections.emptyList());

        //Verify
        moduleValidator.validate(module);
    }

    @Test(expected = ValidationFailedException.class)
    public void testNoName() throws ValidationFailedException {
        //Prepare
        Module module = new DefaultModuleImpl(null, Collections.emptyList());

        //Verify
        moduleValidator.validate(module);
    }

    @Test(expected = ValidationFailedException.class)
    public void testCircularDependency() throws ValidationFailedException {
        //Prepare
        Module module = new DefaultModuleImpl("module", Arrays.asList("module", "module2"));

        //Verify
        moduleValidator.validate(module);
    }
}
