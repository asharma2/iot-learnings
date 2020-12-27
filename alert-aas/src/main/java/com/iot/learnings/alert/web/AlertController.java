package com.iot.learnings.alert.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.learnings.alert.mapper.AlertMapper;
import com.iot.learnings.alert.service.AlertService;
import com.iot.learnings.dto.AlertDTO;

@RestController
@RequestMapping("/api/v1/alert")
public class AlertController {

	@Autowired
	AlertService alertService;
	@Autowired
	AlertMapper alertMapper;

	@GetMapping
	public ResponseEntity<List<AlertDTO>> retrieveAll() {
		List<AlertDTO> alertDtos = alertService.findAll().stream().map(x -> alertMapper.mapDomainToDTO(x))
				.collect(Collectors.toList());
		return ResponseEntity.ok(alertDtos);
	}

}
