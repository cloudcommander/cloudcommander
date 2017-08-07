package com.cloudcommander.vendor.cqrs.services.strategies;

import java.util.UUID;

public interface UuidGenerationStrategy {
    UUID generate();
}
