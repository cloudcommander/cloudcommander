package com.cloudcommander.vendor.cqrs.bus.dispatchers.strategies;

import java.util.UUID;

public class DefaultUuidGenerationStrategy implements UuidGenerationStrategy {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
