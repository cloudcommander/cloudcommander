package com.cloudcommander.vendor.application;

import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public interface Application {
    void start(String[] args) throws ApplicationStartException;
}
