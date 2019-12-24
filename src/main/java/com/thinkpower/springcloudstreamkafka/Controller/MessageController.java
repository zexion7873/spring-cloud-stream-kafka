package com.thinkpower.springcloudstreamkafka.Controller;

import com.thinkpower.springcloudstreamkafka.Service.MessageService;
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
public class MessageController {

    @Autowired
    MessageService messageService;

    // get the String message via HTTP, publish it to broker using spring cloud stream
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void publishMessageString(@RequestBody String payload) {
        messageService.sendMessage(payload);
    }
}

