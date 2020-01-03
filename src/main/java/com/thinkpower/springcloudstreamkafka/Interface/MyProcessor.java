package com.thinkpower.springcloudstreamkafka.Interface;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyProcessor {

    String INPUT = "my-in";

    @Input(INPUT)
    SubscribableChannel input();

    String OUTPUT = "my-out";

    @Output(OUTPUT)
    MessageChannel output();
}
