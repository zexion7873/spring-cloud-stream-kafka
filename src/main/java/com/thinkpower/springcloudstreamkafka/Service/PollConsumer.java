package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.Interface.PolledProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableBinding({PolledProcessor.class})
@EnableScheduling
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
            polledProcessor.input().poll(m -> {
                logger.info("get Message : {}", m);
                logger.info("key : {}", m.getHeaders().getOrDefault("key", null));
            });
        }
}

//    @InboundChannelAdapter(value = PolledProcessor.OUTPUT,
//            poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
//    public Message<String> test() {
//        logger.info("send Message");
//        return  MessageBuilder.withPayload("aaa").build();
//    }

}
