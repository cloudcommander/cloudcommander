package com.cloudcommander.vendor.cqrs.bus;


import com.cloudcommander.vendor.cqrs.Message;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@Wither
public class MessageWrapper<T extends Message>{
    UUID uuid;
    T message;
    Date created;
}