package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.BookDTO;
import com.thinkpower.springcloudstreamkafka.Interface.PolledProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private Producer producer;

    public void sendMessage(BookDTO bookDTO) {
        // send message to channel output
        Message<BookDTO> message = MessageBuilder
                .withPayload(bookDTO)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("key", bookDTO.getName() + System.currentTimeMillis())
                .build();

        producer.getMysource().output().send(message);

        logger.info("send message from myProject : {}", message);
    }
}
