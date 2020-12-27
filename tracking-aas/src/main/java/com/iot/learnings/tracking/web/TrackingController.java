package com.iot.learnings.tracking.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.learnings.dto.TrackingDTO;
import com.iot.learnings.event.Event;
import com.iot.learnings.event.TrackingEvent;
import com.iot.learnings.tracking.mapper.TrackingMapper;
import com.iot.learnings.tracking.model.Tracking;
import com.iot.learnings.tracking.service.TrackingService;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

	@Autowired
	TrackingService trackingService;
	@Autowired
	TrackingMapper trackingMapper;
	@Autowired
	KafkaTemplate<String, Event> eventKafkaTemplate;
	@Value("${topic.tracking.event:tracking.event}")
	String eventTopic;

	@PostMapping
	public ResponseEntity<TrackingDTO> save(@RequestBody TrackingDTO trackingDTO) {
		Tracking tracking = trackingMapper.mapDTOToDomain(trackingDTO);
		trackingService.save(tracking);
		TrackingEvent event = trackingMapper.mapDomainToEvent(tracking);
		event.setEventDate(LocalDateTime.now());
		eventKafkaTemplate.send(eventTopic, event.getImei(), event);
		return ResponseEntity.ok(trackingMapper.mapDomainToDTO(tracking));
	}

	@GetMapping("/{imei}")
	public ResponseEntity<List<TrackingDTO>> findByImei(@PathVariable("imei") String imei) {
		List<Tracking> trackings = trackingService.findByImei(imei);
		List<TrackingDTO> trackingDtos = trackings.stream().map(x -> trackingMapper.mapDomainToDTO(x))
				.collect(Collectors.toList());
		return ResponseEntity.ok(trackingDtos);
	}
}
