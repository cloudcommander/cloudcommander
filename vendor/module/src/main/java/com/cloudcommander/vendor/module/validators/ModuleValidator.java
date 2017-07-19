package com.cloudcommander.vendor.module.validators;

import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public interface ModuleValidator {
    void validate(final Module module) throws ValidationFailedException;
}
