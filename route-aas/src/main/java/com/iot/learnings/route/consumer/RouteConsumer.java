package com.iot.learnings.route.consumer;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.iot.learnings.dto.ReverseGeoRequestDTO;
import com.iot.learnings.dto.ReverseGeoResponseDTO;
import com.iot.learnings.event.DriverEvent;
import com.iot.learnings.event.Event;
import com.iot.learnings.event.RouteEvent;
import com.iot.learnings.route.exception.RouteException;
import com.iot.learnings.route.mapper.RouteMapper;
import com.iot.learnings.route.model.Route;
import com.iot.learnings.route.service.RouteService;

import reactor.core.publisher.Mono;

@Component
public class RouteConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(RouteConsumer.class);

	@Value("${topic.route.event:route.event}")
	String routeEventTopic;
	@Autowired
	RouteService routeService;
	@Autowired
	WebClient revGeoWebClient;
	@Autowired
	RouteMapper routeMapper;
	@Autowired
	KafkaTemplate<String, Event> eventKafkaTemplate;

	@KafkaListener(topics = "${topic.driver.event:driver.event}", //
			groupId = "${spring.kafka.consumer.group-id:route-aas}", //
			containerFactory = "kafkaListenerContainerFactory")
	public void ariEvent(@Header(required = false) MessageHeaders headers, @Payload DriverEvent driverEvent) {
		LOG.info("DriverEvent => Event: {}", driverEvent);
		Route route = new Route();
		route.setEventDate(driverEvent.getEventDate());
		route.setImei(driverEvent.getImei());
		route.setLat(driverEvent.getLat());
		route.setLon(driverEvent.getLon());
		route.setSpeed(driverEvent.getSpeed());
		route.setUsername(driverEvent.getUsername());
		route.setVehicleNo(driverEvent.getVehicleNo());
		setCityQuietly(route);

		RouteEvent routeEvent = routeMapper.mapDomainToEvent(route);
		eventKafkaTemplate.send(routeEventTopic, routeEvent.getImei(), routeEvent);
		routeService.save(route);

	}

	private void setCityQuietly(Route route) {
		try {
			ReverseGeoRequestDTO requestDTO = new ReverseGeoRequestDTO();
			requestDTO.setLat(route.getLat());
			requestDTO.setLon(route.getLon());

			Mono<ReverseGeoResponseDTO> monoReverseGeo = revGeoWebClient.post().uri("/api/v1/reversegeo/revgeo")
					.body(BodyInserters.fromValue(requestDTO)).retrieve()
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new RouteException()))
					.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RouteException()))
					.bodyToMono(ReverseGeoResponseDTO.class).timeout(Duration.ofMillis(1000));

			ReverseGeoResponseDTO dto = monoReverseGeo.block();
			route.setCity(dto.getCity());
		} catch (RouteException e) {
			LOG.error("Exception while calling the revgeo endpoint.", e);
		}
	}
}
