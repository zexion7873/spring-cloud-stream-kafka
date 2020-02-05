package com.thinkpower.springcloudstreamkafka.Interface;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyProcessor {

    String INPUT = "myInput";

    @Input(INPUT)
    SubscribableChannel input();

    String OUTPUT = "myOutput";

    @Output(OUTPUT)
    MessageChannel output();

    String POLLIN = "poll-in";

    @Input(POLLIN)
    PollableMessageSource pollIn();

    String POLLOUT = "poll-out";

    @Output(POLLOUT)
    MessageChannel pollOut();

    String DLQOUT = "dlq-out";

    @Output(DLQOUT)
    MessageChannel dlqOut();

}
