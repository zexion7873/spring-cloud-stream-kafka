package com.thinkpower.springcloudstreamkafka.Utils;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;

/**
 * @從Message中提取partitionKey
 */
public class PartitionKeyExtractor implements PartitionKeyExtractorStrategy {

    @Override
    public Object extractKey(Message<?> message) {

        if (message.getPayload() instanceof MessageDTO) {
            MessageDTO messageDTO = (MessageDTO) message.getPayload();

            return  messageDTO.getId();
        }

        return null;
    }
}
