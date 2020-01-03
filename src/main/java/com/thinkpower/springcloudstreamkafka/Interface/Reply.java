package com.thinkpower.springcloudstreamkafka.Interface;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Reply {

    String INPUT = "reply-in";

    @Input(INPUT)
    SubscribableChannel replyInput();

    String OUTPUT = "reply-out";

    @Output(OUTPUT)
    MessageChannel replyOutput();

}
