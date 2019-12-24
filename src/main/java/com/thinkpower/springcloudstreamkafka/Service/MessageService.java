package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.Controller.MessageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private Producer producer;

    public void sendMessage(String payload) {
        // send message to channel output
        logger.info("send message from myProject : " + payload);
        producer.getMysource().output()
                .send(MessageBuilder.withPayload(payload).setHeader("type", "string").build());

    }
}
