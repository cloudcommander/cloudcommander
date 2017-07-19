package com.cloudcommander.vendor.module.validators;

import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public interface ModuleCollectionValidator {

    void validate(final Collection<Module> moduleCollection) throws ValidationFailedException;

}
