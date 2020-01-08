package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.Interface.PolledProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

@EnableBinding({PolledProcessor.class})
//@EnableScheduling
public class PollConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PollConsumer.class);

    @Autowired
    PolledProcessor polledProcessor;

    @Autowired
    Producer producer;

    @Scheduled(fixedDelay = 5_000)
    public void poll() {
        logger.info("Start Poll Message");
        // 拉所有 Message
        boolean hasMessage = true;
        while (hasMessage) {
            polledProcessor.input().poll(message -> {
                logger.info("myProject Received a message : {}", message);
                logger.info("uuid : {}", message.getHeaders().getOrDefault("uuid", null));
                logger.info("partition : {}", message.getHeaders().getOrDefault("kafka_receivedPartitionId", null));
                logger.info("offset : {}", message.getHeaders().getOrDefault("kafka_offset", null));
            });
        }
    }


    @Bean
    @StreamRetryTemplate
    public ApplicationRunner runner(PollableMessageSource pollIn,
                                    @Qualifier("reply-out") MessageChannel pollOut) {
        return args -> {
            while (true) {
                boolean result = pollIn.poll(message -> {
                    logger.info("myProject Received a message : {}", message);
                    logger.info("uuid : {}", message.getHeaders().getOrDefault("uuid", null));
                    logger.info("partition : {}", message.getHeaders().getOrDefault("kafka_receivedPartitionId", null));
                    logger.info("offset : {}", message.getHeaders().getOrDefault("kafka_offset", null));

                    String payload = (String) message.getPayload();
                    logger.info("Received: " + payload);
                    pollOut.send(MessageBuilder.withPayload(payload)
                            .copyHeaders(message.getHeaders())
                            .build());

                }
//                , new ParameterizedTypeReference<String>() {}
                );
                if (result) {
                    logger.info("Processed a message");
                }
                else {
                    logger.info("Nothing to do");
                }
                Thread.sleep(1_000);
            }
        };
    }



}
