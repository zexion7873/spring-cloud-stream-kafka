package com.thinkpower.springcloudstreamkafka.Utils;

import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.util.ObjectUtils;

/**
 * @决定message发送到哪个partition的策略
 */
public class PartitionSelector implements PartitionSelectorStrategy {

    @Override
    public int selectPartition(Object key, int partitionCount) {

        if (!ObjectUtils.isEmpty(key)) {
            Long id = (Long) key;

            return id.intValue() % partitionCount;
        }

        return 0;
    }
}
