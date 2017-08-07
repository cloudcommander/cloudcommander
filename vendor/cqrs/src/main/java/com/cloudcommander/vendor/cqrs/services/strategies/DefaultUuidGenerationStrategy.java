package com.cloudcommander.vendor.cqrs.services.strategies;

import java.util.UUID;

public class DefaultUuidGenerationStrategy implements UuidGenerationStrategy {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
