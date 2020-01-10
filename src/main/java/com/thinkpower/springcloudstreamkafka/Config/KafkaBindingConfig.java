package com.thinkpower.springcloudstreamkafka.Config;

import com.thinkpower.springcloudstreamkafka.Utils.PartitionKeyExtractor;
import com.thinkpower.springcloudstreamkafka.Utils.PartitionSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaBindingConfig {

    @Bean("PartitionKeyExtractor")
    public PartitionKeyExtractor partitionKeyExtractor() {
        return new PartitionKeyExtractor();
    }

    @Bean("PartitionSelector")
    public PartitionSelector partitionSelector() {
        return new PartitionSelector();
    }

}
