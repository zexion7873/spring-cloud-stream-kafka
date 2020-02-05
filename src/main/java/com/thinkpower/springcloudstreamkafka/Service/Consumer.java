package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@EnableBinding({MyProcessor.class})
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Resource
    private MyProcessor myProcessor;

    private static final String RETRIES_HEADER = "retries";

    private final AtomicInteger processed = new AtomicInteger();

    /**
     * 訊息監聽, 回覆
     * @param message 接收到的 Message
     */
    @StreamListener(MyProcessor.INPUT)
    @SendTo(MyProcessor.OUTPUT)
    public Message<MessageDTO> consume(Message<MessageDTO> message) {

        logger.info("myApplication Received a message : {}", message);
        logger.info("payload : {}", message.getPayload());
        logger.info("receivedTopic : {}", message.getHeaders().getOrDefault(KafkaHeaders.RECEIVED_TOPIC, null));
        logger.info("replyTopic : {}", message.getHeaders().getOrDefault(KafkaHeaders.REPLY_TOPIC, null));
        logger.info("offset : {}", message.getHeaders().getOrDefault(KafkaHeaders.OFFSET, null));

        if (message.getPayload().getId() %2 != 0) {
            throw new RuntimeException("BOOM!");
        } else {
            MessageDTO messageDTO = message.getPayload();
            messageDTO.setHasConsume(true);
            // 手動 commit offset
            offsetCommit(message);
            // 組成回覆的 Message 物件
            return MessageBuilder
                    .withPayload(messageDTO)
                    .copyHeaders(message.getHeaders())
                    .build();
        }
    }

    @ServiceActivator(inputChannel = "testTopic.myGroup.errors")
    public void error(ErrorMessage errorMessage) {
        System.err.println("In Error");
        System.err.println("errorMessage : " + errorMessage);
        System.err.println("OriginalMessage : " + errorMessage.getOriginalMessage());
        errorMessage.getPayload().getMessage();

//        myProcessor.output().send(errorMessage.getOriginalMessage());
    }
//    @StreamListener("errorChannel")
//    public void errorGlobal(ErrorMessage errorMessage) {
//        System.err.println("In ErrorGlobal");
//        System.err.println("ErrorMessage : " + errorMessage);
//        System.err.println("OriginalMessage : " + errorMessage.getPayload());
//    }


//    @StreamListener(MyProcessor.INPUT)
//    @SendTo(MyProcessor.OUTPUT)
    public Message<?> reRoute(Message<?> failed) {
        processed.incrementAndGet();
        Integer retries = failed.getHeaders().get(RETRIES_HEADER, Integer.TYPE);
        if (retries == null) {
            System.out.println("First retry for " + failed);
            return MessageBuilder.fromMessage(failed)
                    .setHeader(RETRIES_HEADER,1)
                    .setHeader(BinderHeaders.PARTITION_OVERRIDE,
                            failed.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID))
                    .build();
        }
        else if (retries < 3) {
            System.out.println("Another retry for " + failed);
            return MessageBuilder.fromMessage(failed)
                    .setHeader(RETRIES_HEADER, retries + 1)
                    .setHeader(BinderHeaders.PARTITION_OVERRIDE,
                            failed.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID))
                    .build();
        }
        else {
            System.out.println("Retries exhausted for " + failed);
            myProcessor.dlqOut().send(MessageBuilder.fromMessage(failed)
                    .setHeader(BinderHeaders.PARTITION_OVERRIDE,
                            failed.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID))
                    .build());
        }
        return null;
    }

    /**
     * 手動 Commit Offset
     * @param message 訊息
     */
    private void offsetCommit(Message<?> message) {
        Acknowledgment acknowledgment = message.getHeaders()
                .get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        logger.info("Acknowledgment : {}", acknowledgment);

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

}

