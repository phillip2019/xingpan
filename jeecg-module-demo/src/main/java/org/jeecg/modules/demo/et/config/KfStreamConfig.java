package org.jeecg.modules.demo.et.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.jeecg.modules.demo.et.entity.EventTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * @Author: xiaowei.song
 * @Date: 2022/12/03
 * @description: kafka stream 配置类
 */
@Configuration
@EnableKafkaStreams
public class KfStreamConfig {
    private static final Logger logger = LoggerFactory.getLogger(KfStreamConfig.class);

    @Bean
    public KStream<String, EventTracking> eventTrackingStream(StreamsBuilder streamsBuilder, @Value("cg.et.topic")String topic) {
        KStream<String, String> stream = streamsBuilder.stream(topic, Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, EventTracking> etStream = stream.map((key, value) -> {
            EventTracking et = EventTracking.of(value);
            return new KeyValue<>(et.getAnonymousId(), et);
        });
        return etStream;
    }
}
