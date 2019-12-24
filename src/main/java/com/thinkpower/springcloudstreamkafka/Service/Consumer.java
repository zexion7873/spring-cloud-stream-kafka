package com.thinkpower.springcloudstreamkafka.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding({Sink.class})
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    Producer producer;

    @StreamListener(target = Sink.INPUT)
    public void consume(String message) {
        logger.info("myProject received a string message : " + message);
        replyMessage("replyMessage");
    }

    private void replyMessage(String message) {
        producer.getMysource().output().send(MessageBuilder.withPayload(message).setHeader("type", "string").build());
    }
}

