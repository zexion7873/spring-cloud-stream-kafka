package com.thinkpower.springcloudstreamkafka.Service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private static final String KAFKA_HOST = "localhost";
    private static final int KAFKA_PORT = 9092;

    private static AdminClient admin;

    /**
     *
     * @param topic topic 名稱
     * @param brokerID broker ID
     * @return long
     */
    public long getTopicSizeForBroker(String topic, int brokerID) {
        initialize();
        long topicSize = 0L;
        try {
            topicSize = getTopicDiskSizeForSomeBroker(topic, brokerID);
        } catch (Exception e) {
            logger.info("getTopicSizeForBroker error : {}", e.getMessage());
        } finally {
            shutdown();
        }

        return topicSize;
    }


    private long getTopicDiskSizeForSomeBroker(String topic, int brokerID)
            throws ExecutionException, InterruptedException {
        long sum = 0;
        DescribeLogDirsResult ret = admin.describeLogDirs(Collections.singletonList(brokerID));
        Map<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> tmp = ret.all().get();
        for (Map.Entry<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> entry : tmp.entrySet()) {
            Map<String, DescribeLogDirsResponse.LogDirInfo> tmp1 = entry.getValue();
            for (Map.Entry<String, DescribeLogDirsResponse.LogDirInfo> entry1 : tmp1.entrySet()) {
                DescribeLogDirsResponse.LogDirInfo info = entry1.getValue();
                Map<TopicPartition, DescribeLogDirsResponse.ReplicaInfo> replicaInfoMap = info.replicaInfos;
                for (Map.Entry<TopicPartition, DescribeLogDirsResponse.ReplicaInfo> replicas : replicaInfoMap.entrySet()) {
                    if (topic.equals(replicas.getKey().topic())) {
                        sum += replicas.getValue().size;
                    }
                }
            }
        }
        return sum;
    }

    private static void initialize() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST + ":" + KAFKA_PORT);
        admin = AdminClient.create(props);
    }

    private static void shutdown() {
        if (admin != null) {
            admin.close();
        }
    }

}
