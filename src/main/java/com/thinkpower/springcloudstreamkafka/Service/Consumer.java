package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.BookDTO;
import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import com.thinkpower.springcloudstreamkafka.Interface.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding({Reply.class, MyProcessor.class})
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @StreamListener(target = MyProcessor.INPUT)
    @SendTo({Reply.OUTPUT})
    public BookDTO consume(Message<BookDTO> message) {

//        Acknowledgment acknowledgment = message.getHeaders()
//                .get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
//
//        System.err.println("Acknowledgment : " + acknowledgment);
//        if (acknowledgment != null) {
//            System.out.println("Acknowledgment provided");
//            acknowledgment.acknowledge();
//        }
        logger.info("myProject Received a message : {}", message);
        logger.info("uuid : {}", message.getHeaders().getOrDefault("uuid", null));
        logger.info("partition : {}", message.getHeaders().getOrDefault("kafka_receivedPartitionId", null));
        logger.info("offset : {}", message.getHeaders().getOrDefault("kafka_offset", null));

        return message.getPayload();
    }

    @StreamListener("errorChannel")
    public void error(Message<?> message) {
        System.out.println("Handling ERROR: " + message);
    }

    @StreamListener(Reply.INPUT)
    public void reply(BookDTO reply) {
//        logger.info("Reply Book : {}", reply.getName());
    }


}

