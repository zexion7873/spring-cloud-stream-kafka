package com.thinkpower.springcloudstreamkafka.Controller;

import com.thinkpower.springcloudstreamkafka.Service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "kafka")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    KafkaService kafkaService;

    @RequestMapping(value = "/getTopicSizeForBroker", method = RequestMethod.POST)
    public long getTopicSizeForBroker(@RequestParam("topic") String topic, @RequestParam("brokerID") int brokerID) {
        return kafkaService.getTopicSizeForBroker(topic, brokerID);
    }

}
