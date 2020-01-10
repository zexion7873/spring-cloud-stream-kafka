package com.thinkpower.springcloudstreamkafka.Utils;

import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;

/**
 * @从Message中提取partitionkey的策略
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
