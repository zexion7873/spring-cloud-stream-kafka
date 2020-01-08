package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.BookDTO;
import com.thinkpower.springcloudstreamkafka.Interface.PolledProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    Producer producer;

    @Autowired
    PolledProcessor polledProcessor;

    public void sendMessage(BookDTO bookDTO) {
        // send message to channel output
        Message<BookDTO> message = MessageBuilder
                .withPayload(bookDTO)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("uuid", UUID.randomUUID())
                .build();

        producer.getMysource().output().send(message);
        logger.info("send message from my-proj : {}", message);

    }

//    @InboundChannelAdapter(channel = PolledProcessor.OUTPUT,
//            poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "100"))
    public Message<BookDTO> sendPollMessage() {
        BookDTO bookDTO = new BookDTO("ccc", "author", "20200107");

        Message<BookDTO> message = MessageBuilder
                .withPayload(bookDTO)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("uuid", UUID.randomUUID())
                .build();

        polledProcessor.output().send(message);

        return message;
    }
}
