package com.thinkpower.springcloudstreamkafka.Controller;

import com.thinkpower.springcloudstreamkafka.Service.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private Producer producer;

    // get the String message via HTTP, publish it to broker using spring cloud stream
    @RequestMapping(value = "/sendMessage/string", method = RequestMethod.POST)
    public String publishMessageString(@RequestBody String payload) {
        // send message to channel output
        logger.info("send message from myProject : " + payload);
        boolean send = producer.getMysource().output().send(MessageBuilder.withPayload(payload).setHeader("type", "string").build());
        if (send) {
            return "success";
        }
        return "fail";
    }
}

