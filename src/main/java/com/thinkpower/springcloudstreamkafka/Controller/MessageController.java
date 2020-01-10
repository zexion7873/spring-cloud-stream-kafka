package com.thinkpower.springcloudstreamkafka.Controller;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import com.thinkpower.springcloudstreamkafka.Service.MessageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    @Resource
    private MessageService messageService;

    // get the String message via HTTP, publish it to broker using spring cloud stream
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void publishMessageString(@RequestBody MessageDTO payload) {
        messageService.sendMessage(payload);
    }
}

