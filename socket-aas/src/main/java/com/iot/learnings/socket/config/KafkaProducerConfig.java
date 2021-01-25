package com.iot.learnings.socket.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.iot.learnings.event.Event;

@Configuration
public class KafkaProducerConfig {

	@Value("${spring.kafka.producer.bootstrap-servers}")
	String bootstrapServer;

	/**
	 * <code>
	 * acks = 0 - producer  does not wait for acknowledgement
	 * acks = 1 - leader will write record to its local log
	 * acks = all - leader will wait for full set of in-sync replicas to acknowledge the records
	 * </code>
	 */
	@Value("${spring.kafka.producer.acks:all}")
	String acks;
	/**
	 * <code>
	 * Max size of request in bytes
	 * default - 1048576
	 * </code>
	 */
	@Value("${spring.kafka.producer.properties.max.request.size:65536}")
	Integer maxRequestSize;
	/**
	 * <code>
	 * none -
	 * gzip -
	 * snappy -
	 * lz4 -
	 * zstd -
	 * </code>
	 */
	@Value("${spring.kafka.producer.properties.compression.type:none}")
	String compressionType;
	/**
	 * <code>
	 * Batch records together into fewer requests.
	 * Small Batch Size - reduce throughput
	 * Large Batch Size - wasteful
	 * </code>
	 */
	@Value("${spring.kafka.producer.properties.batch.size:16384}")
	Integer batchSize;
	/**
	 * <code>
	 * 
	 * </code>
	 */
	@Value("${spring.kafka.producer.properties.retries:3}")
	Integer retries;
	/**
	 * <code>
	 * producer groups together any records that arrive in between request transmissions into single batch request.
	 * Once we get batch.size worth of records for partition, it will send immediately.
	 * </code>
	 */
	@Value("${spring.kafka.producer.properties.linger.ms:0}")
	Integer lingerMs;

	public Map<String, Object> defaultProperties() {
		final Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.ACKS_CONFIG, acks);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
		return props;
	}

	/**
	 * <code>
	 * enable.idempotence - true
	 * the producer will ensure that exactly one copy of message is written in the stream
	 * max.in.flight.requests.per.connection <= 5
	 * retries > 0
	 * acks = all
	 * 
	 * 
	 * </code>
	 * 
	 * @return
	 */
	public Map<String, Object> idempotenceProperties() {
		final Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
		/**
		 * Idempotence
		 */
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.ACKS_CONFIG, acks);
		return props;
	}

	@Bean
	public ProducerFactory<String, Event> iotEventKafkaProducerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
		/**
		 * Idempotence
		 */
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		props.put(ProducerConfig.ACKS_CONFIG, acks);
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);

		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<String, Event> iotEventKafkaTemplate() {
		return new KafkaTemplate<>(iotEventKafkaProducerFactory());
	}

}
