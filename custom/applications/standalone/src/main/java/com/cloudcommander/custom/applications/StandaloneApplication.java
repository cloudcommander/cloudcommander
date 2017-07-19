package com.cloudcommander.custom.applications;

import com.cloudcommander.vendor.application.Application;
import com.cloudcommander.vendor.application.DefaultApplicationImpl;
import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class StandaloneApplication {
    public static void main (String [ ] args) throws ApplicationStartException {
        Application application = new DefaultApplicationImpl();
        application.start(args);
    }
}
