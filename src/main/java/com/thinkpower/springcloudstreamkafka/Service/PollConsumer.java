package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding
public class PollConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PollConsumer.class);

    @Resource
    private MyProcessor myProcessor;

    /**
     *
     */
//    @Scheduled(cron = "* 0/2 * * * ?")
    public void poll() {
        logger.info("Start Scheduled Poll Message");
        while (true) {
            myProcessor.pollIn().poll(message -> {

                MessageDTO messageDTO = (MessageDTO) message.getPayload();

                logger.info("myProject Received a message : {}", message);
                logger.info("id : {}", messageDTO.getId());

                messageDTO.setHasConsume(true);
                myProcessor.pollOut().send(MessageBuilder.withPayload(messageDTO)
                        .copyHeaders(message.getHeaders())
                        .build());
                logger.info("Processed a message");
            }, new ParameterizedTypeReference<MessageDTO>() {});
        }

    }


    /**
     * Poll Message
     * @param pollIn 訂閱通道
     * @param pollOut 推播通道
     * @return ApplicationRunner
     */
//    @Bean
//    public ApplicationRunner poller(PollableMessageSource pollIn,
//                                    @Qualifier("my-out") MessageChannel pollOut) {
//        return args -> {
//            // some condition
//            while (true) {
//                try {
//                    if (!pollIn.poll(message -> {
//                                MessageDTO messageDTO = (MessageDTO) message.getPayload();
//
//                                logger.info("myProject Received a message : {}", message);
//                                logger.info("id : {}", messageDTO.getId());
//
//                                messageDTO.setHasConsume(true);
//                                pollOut.send(MessageBuilder.withPayload(messageDTO)
//                                        .copyHeaders(message.getHeaders())
//                                        .build());
//                                logger.info("Processed a message");
//                            },
//                            new ParameterizedTypeReference<MessageDTO>() {})
//                    ) {
//                        Thread.sleep(2_000);
//                    }
//                } catch (Exception e) {
//                    logger.info("poller error : {}", e.getMessage());
//                }
//            }
//        };
//    }
}
