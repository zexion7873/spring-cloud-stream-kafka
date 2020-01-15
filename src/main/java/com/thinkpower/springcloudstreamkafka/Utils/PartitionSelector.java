package com.thinkpower.springcloudstreamkafka.Utils;

import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.util.ObjectUtils;

/**
 * @決定message發送到哪個partition
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
