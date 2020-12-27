package com.iot.learnings.route.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.consumer.bootstrap-servers}")
	String bootstrapServer;

	@Value("${spring.kafka.consumer.group-id:route-aas}")
	String groupId;

	@Value("#{T(Integer).parseInt('${spring.kafka.listener.concurrency:1}')}")
	Integer listenerConcurrency;

	@Value("#{T(Integer).parseInt('${spring.kafka.consumer.max-poll-interval-ms:90000}')}")
	Integer maxPollIntervalMs;

	@Bean
	Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return props;
	}

	@Bean
	ConsumerFactory<String, Object> consumerFactory() {
		JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
		jsonDeserializer.addTrustedPackages("com.iot.learnings.event");
		jsonDeserializer.setRemoveTypeHeaders(false);
		jsonDeserializer.setUseTypeMapperForKey(false);
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), jsonDeserializer);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setAckMode(AckMode.RECORD);
		factory.getContainerProperties().setPollTimeout(maxPollIntervalMs);
		factory.getContainerProperties().setSyncCommits(false);
		factory.setConcurrency(listenerConcurrency);
		factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(0L, 2L)));
		return factory;
	}
}
