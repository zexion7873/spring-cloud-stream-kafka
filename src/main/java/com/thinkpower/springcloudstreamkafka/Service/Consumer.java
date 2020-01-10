package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

@EnableBinding({MyProcessor.class})
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private MyProcessor myProcessor;

    /**
     *
     * @param message 接收到的 Message
     * @return Message<MessageDTO>
     */
    @StreamListener(target = MyProcessor.INPUT)
    @SendTo({MyProcessor.OUTPUT})
    public Message<MessageDTO> consume(Message<MessageDTO> message) {

        logger.info("myProject Received a message : {}", message);
        logger.info("id : {}", message.getPayload().getId());

        MessageDTO payload = checkConsume(message.getPayload());
        offsetCommit(message);

        return MessageBuilder.withPayload(payload).copyHeaders(message.getHeaders()).build();
    }

    /**
     * Poll Message
     * @param pollIn 訂閱通道
     * @param pollOut 推播通道
     * @return ApplicationRunner
     */
    @Bean
    public ApplicationRunner poller(PollableMessageSource pollIn,
                                    @Qualifier("my-out") MessageChannel pollOut) {
        return args -> {
            // some condition
            while (true) {
                boolean result = pollIn.poll(message -> {

                    logger.info("myProject Received a message : {}", message);
                    MessageDTO payload = checkConsume((MessageDTO) message.getPayload());
                    logger.info("Received: " + payload);
                    pollOut.send(MessageBuilder.withPayload(payload)
                            .copyHeaders(message.getHeaders())
                            .build());
                        }, new ParameterizedTypeReference<MessageDTO>() {}
                    );
                if (result) {
                    logger.info("Processed a message");
                }
                else {
                    logger.info("Nothing to do");
                    Thread.sleep(1_000);
                }
            }
        };
    }


//    @StreamListener(target = MyProcessor.INPUT, condition = "headers['key'] %2 == 0")
//    public void consume(Message<MessageDTO> message) {
//
//        logger.info("myProject Received a message : {}", message);
//        logger.info("id : {}", message.getPayload().getId());
//
//        if (message.getPayload() instanceof MessageDTO) {
//            MessageDTO messageDTO = message.getPayload();
//            messageDTO.setHasConsume(true);
//            Message<MessageDTO> replyMessage = MessageBuilder
//                    .withPayload(messageDTO)
//                    .copyHeaders(message.getHeaders())
//                    .build();
//            myProcessor.output().send(replyMessage);
//        }
//
//        offsetCommit(message);
//
//    }




    @StreamListener("errorChannel")
    public void error(Message<MessageDTO> message) {
        System.out.println("Handling ERROR: " + message);
    }

    private MessageDTO checkConsume(MessageDTO payload) {
        payload.setHasConsume(true);

        return  payload;
    }

    /**
     * 手動 Commit Offset
     * @param message 訊息
     */
    private void offsetCommit(Message<MessageDTO> message) {
        Acknowledgment acknowledgment = message.getHeaders()
                .get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        logger.info("Acknowledgment : {}", acknowledgment);

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }



    @Scheduled(fixedDelay = 5_000)
    public void poll() {
        logger.info("Start Poll Message");
        // 拉所有 Message
        boolean hasMessage = true;
        while (hasMessage) {
            myProcessor.pollIn().poll(message -> {
                logger.info("myProject Received a message : {}", message);
                MessageDTO payload = checkConsume((MessageDTO) message.getPayload());
                logger.info("id : {}", payload.getId());
            });
        }
    }


}

