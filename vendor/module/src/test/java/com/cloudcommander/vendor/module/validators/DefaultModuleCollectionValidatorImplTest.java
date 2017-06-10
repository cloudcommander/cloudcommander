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
public class DefaultModuleCollectionValidatorImplTest {

    private final ModuleValidator moduleValidator = new DefaultModuleValidatorImpl();

    private final DefaultModuleCollectionValidatorImpl moduleCollectionValidator = new DefaultModuleCollectionValidatorImpl(moduleValidator);

    @Test(expected = ValidationFailedException.class)
    public void testInvalidSingle() throws ValidationFailedException {
        //Prepare
        Module invalidExtension = new DefaultModuleImpl(null, Collections.emptyList());

        //Verify
        moduleCollectionValidator.validate(Collections.singletonList(invalidExtension));
    }

    @Test
    public void testValid() throws ValidationFailedException {
        //Prepare
        Module module1 = new DefaultModuleImpl("module1", Collections.emptyList());
        Module module2 = new DefaultModuleImpl("module2", Collections.emptyList());
        Module module3 = new DefaultModuleImpl("module3", Collections.emptyList());

        //Verify
        moduleCollectionValidator.validate(Arrays.asList(module1, module2, module3));
    }

    @Test(expected = ValidationFailedException.class)
    public void testRepeatedName() throws ValidationFailedException {
        //Prepare
        Module module1 = new DefaultModuleImpl("module2", Collections.emptyList());
        Module module2 = new DefaultModuleImpl("module2", Collections.emptyList());
        Module module3 = new DefaultModuleImpl("module3", Collections.emptyList());

        //Verify
        moduleCollectionValidator.validate(Arrays.asList(module1, module2, module3));
    }
}
