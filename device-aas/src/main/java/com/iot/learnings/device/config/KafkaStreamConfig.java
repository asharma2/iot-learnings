package com.iot.learnings.device.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.iot.learnings.device.service.DeviceService;
import com.iot.learnings.event.DeviceEvent;
import com.iot.learnings.event.TrackingEvent;

import brave.kafka.streams.KafkaStreamsTracing;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

	private Serde<TrackingEvent> trackingEventSerde = Serdes.serdeFrom(new JsonSerializer<>(),
			new JsonDeserializer<>(TrackingEvent.class));

	@Value("${topic.tracking.event:tracking.event}")
	String trackingEventTopic;

	@Value("${topic.device.event:device.event}")
	String deviceEventTopic;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.kafka")
	KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

	@Primary
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kStreamsConfigs(KafkaProperties kafkaProperties,
			@Value("${spring.application.name}") String appName) {
		Map<String, Object> props = new HashMap<>();
		props.putAll(kafkaProperties.getProperties());

		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, appName + "-stream");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
		props.put(StreamsConfig.STATE_DIR_CONFIG, "data");
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonNode.class);
		props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
				LogAndContinueExceptionHandler.class);
		props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());

		return new KafkaStreamsConfiguration(props);
	}

	@Bean("trackingEventStream")
	public KStream<String, TrackingEvent> trackingEventStream(StreamsBuilder builder, DeviceService deviceService,
			KafkaStreamsTracing kafkaStreamsTracing) {
		KStream<String, TrackingEvent> inputStream = builder.stream(trackingEventTopic,
				Consumed.with(Serdes.String(), trackingEventSerde));

		ValueMapper<TrackingEvent, DeviceEvent> eventTransformation = new ValueMapper<TrackingEvent, DeviceEvent>() {
			@Override
			public DeviceEvent apply(TrackingEvent value) {
				DeviceEvent event = new DeviceEvent();
				event.setDirection(value.getDirection());
				event.setEventDate(value.getEventDate());
				event.setImei(value.getImei());
				event.setLat(value.getLat());
				event.setLon(value.getLon());
				event.setSpeed(value.getSpeed());
				return event;
			}
		};

		inputStream.transform(kafkaStreamsTracing.filter("correlationId",
				(k, v) -> deviceService.findByImei(v.getImei()).isPresent()));

		inputStream.transformValues(kafkaStreamsTracing.mapValues("correlationId", eventTransformation))
				.to(deviceEventTopic);

		return inputStream;
	}
}
