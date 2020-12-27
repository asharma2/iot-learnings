package com.iot.learnings.alert.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
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
import com.iot.learnings.alert.service.AlertService;
import com.iot.learnings.event.AlertEvent;
import com.iot.learnings.event.RouteEvent;
import com.iot.learnings.model.AlertMetadata;
import com.iot.learnings.model.AlertType;
import com.iot.learnings.model.Severity;

import brave.kafka.streams.KafkaStreamsTracing;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

	private Serde<RouteEvent> routeEventSerde = Serdes.serdeFrom(new JsonSerializer<>(),
			new JsonDeserializer<>(RouteEvent.class));

	@Value("${topic.route.event:route.event}")
	String routeEventTopic;

	@Value("${topic.critical.event:critical.event}")
	String criticalEventTopic;

	@Value("${topic.warning.event:warning.event}")
	String warningEventTopic;

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

	@SuppressWarnings("unchecked")
	@Bean("trackingEventStream")
	public KStream<String, RouteEvent> trackingEventStream(StreamsBuilder builder, AlertService alertService,
			KafkaStreamsTracing kafkaStreamsTracing) {
		KStream<String, RouteEvent> inputStream = builder.stream(routeEventTopic,
				Consumed.with(Serdes.String(), routeEventSerde));

		ValueMapper<RouteEvent, AlertEvent> eventTransformation = new ValueMapper<RouteEvent, AlertEvent>() {
			@Override
			public AlertEvent apply(RouteEvent value) {
				AlertEvent event = new AlertEvent();
				if (value.getSpeed() > 100) {
					event.setAlertType(AlertType.Overspeed);
					event.setSeverity(Severity.Critical);
				} else if (value.getSpeed() > 80) {
					event.setAlertType(AlertType.Overspeed);
					event.setSeverity(Severity.Warning);
				}
				AlertMetadata metadata = new AlertMetadata();
				metadata.put("IMEI", value.getImei());
				metadata.put("SPEED", Double.toString(value.getSpeed()));
				event.setMetadata(metadata);
				return event;
			}
		};

		Predicate<String, AlertEvent> criticalEvent = (k, v) -> v.getSeverity() == Severity.Critical;
		Predicate<String, AlertEvent> warningEvent = (k, v) -> v.getSeverity() == Severity.Warning;

		inputStream.transform(kafkaStreamsTracing.filter("correlationId", (k, v) -> v.getSpeed() > 80));

		KStream<String, AlertEvent>[] branches = inputStream
				.transformValues(kafkaStreamsTracing.mapValues("correlationId", eventTransformation))
				.branch(criticalEvent, warningEvent);

		branches[0].to(criticalEventTopic);
		branches[1].to(warningEventTopic);

		return inputStream;
	}
}
