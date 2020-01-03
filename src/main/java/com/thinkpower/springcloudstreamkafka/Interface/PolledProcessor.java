package com.thinkpower.springcloudstreamkafka.Interface;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;

public interface PolledProcessor {

    String INPUT = "poll-in";
    String OUTPUT = "poll-out";

    @Input(INPUT)
    PollableMessageSource input();
    @Output(OUTPUT)
    MessageChannel output();

}
