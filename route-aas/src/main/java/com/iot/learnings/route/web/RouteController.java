package com.iot.learnings.route.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.learnings.dto.RouteDTO;
import com.iot.learnings.route.mapper.RouteMapper;
import com.iot.learnings.route.service.RouteService;

@RestController
@RequestMapping("/api/v1/route")
public class RouteController {

	@Autowired
	RouteService routeService;
	@Autowired
	RouteMapper routeMapper;

	@GetMapping("/trackposition")
	public ResponseEntity<List<RouteDTO>> trackPosition() {
		List<RouteDTO> trackPositions = routeService.findAll().stream().map(x -> routeMapper.mapDomainToDTO(x))
				.collect(Collectors.toList());
		return ResponseEntity.ok(trackPositions);
	}

}
