package com.cloudcommander.vendor.cqrs.bus.dispatchers.strategies;

import java.util.UUID;

public interface UuidGenerationStrategy {
    UUID generate();
}
