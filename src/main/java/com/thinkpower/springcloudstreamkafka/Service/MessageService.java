package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);


    @Resource
    MyProcessor myProcessor;

    public void sendMessage(MessageDTO dto) {
        // send message to channel output
        Message<MessageDTO> message = MessageBuilder
                .withPayload(dto)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("uuid", UUID.randomUUID())
                .build();

        myProcessor.output().send(message);
        logger.info("send message from my-proj : {}", message);

    }

//    @InboundChannelAdapter(channel = PolledProcessor.OUTPUT,
//            poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "100"))
    public Message<MessageDTO> sendPollMessage() {
        MessageDTO bookDTO = new MessageDTO(0L, "aaa");

        Message<MessageDTO> message = MessageBuilder
                .withPayload(bookDTO)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("uuid", UUID.randomUUID())
                .build();

        myProcessor.pollOut().send(message);

        return message;
    }
}
