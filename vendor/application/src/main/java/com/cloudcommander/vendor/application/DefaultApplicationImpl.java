package com.cloudcommander.vendor.application;

import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;
import com.cloudcommander.vendor.module.loader.DefaultModuleLoaderImpl;
import com.cloudcommander.vendor.module.loader.ModuleLoader;
import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.printer.DefaultModulePrinterImpl;
import com.cloudcommander.vendor.module.printer.ModulePrinter;
import com.cloudcommander.vendor.module.sorter.DefaultModuleSorterImpl;
import com.cloudcommander.vendor.module.sorter.ModuleSorter;
import com.cloudcommander.vendor.module.validators.DefaultModuleCollectionValidatorImpl;
import com.cloudcommander.vendor.module.validators.DefaultModuleValidatorImpl;
import com.cloudcommander.vendor.module.validators.ModuleCollectionValidator;
import com.cloudcommander.vendor.module.validators.ModuleValidator;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultApplicationImpl implements Application{

    private final static Logger LOG = LogManager.getLogger(DefaultApplicationImpl.class);

    @Override
    public void start(String[] args) throws ApplicationStartException {
        final Collection<Module> modules = getAvailableModules();
        try {
            validateModules(modules);
            final List<Module> sortedModules = sortModules(modules); //Modules sorted by loading order

            logApplicationTrailer(sortedModules);
        } catch (ValidationFailedException | IOException e) {
            throw new ApplicationStartException(e);
        }
    }

    private void validateModules(final Collection<Module> modules) throws ValidationFailedException {
        final ModuleValidator moduleValidator = new DefaultModuleValidatorImpl();
        final ModuleCollectionValidator moduleCollectionValidator = new DefaultModuleCollectionValidatorImpl(moduleValidator);

        moduleCollectionValidator.validate(modules);
    }

    protected Collection<Module> getAvailableModules(){
        final ModuleLoader moduleLoader = new DefaultModuleLoaderImpl();
        return moduleLoader.getModules();
    }

    protected List<Module> sortModules(final Collection<Module> modules){
        final ModuleSorter moduleSorter = new DefaultModuleSorterImpl();
        return moduleSorter.sort(modules);
    }

    protected void logApplicationTrailer(final List<Module> sortedModules) throws IOException {
        if(LOG.isInfoEnabled()){
            ModulePrinter modulePrinter = new DefaultModulePrinterImpl();

            try(OutputStream modulesOutputStream = new ByteArrayOutputStream();
                    PrintStream modulesPrintStream = new PrintStream(modulesOutputStream);){
                modulePrinter.print(sortedModules, modulesPrintStream);

                LOG.info("========================================================");
                LOG.info("Starting system:");
                LOG.info("========================================================");
                LOG.info("Active modules:");
                LOG.info(modulesOutputStream);
                LOG.info("========================================================");
            }
        }
    }
}
