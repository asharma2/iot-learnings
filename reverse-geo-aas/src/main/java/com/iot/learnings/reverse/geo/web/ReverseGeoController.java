package com.iot.learnings.reverse.geo.web;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.learnings.dto.ReverseGeoRequestDTO;
import com.iot.learnings.dto.ReverseGeoResponseDTO;
import com.iot.learnings.reverse.geo.mapper.ReverseGeoMapper;
import com.iot.learnings.reverse.geo.model.ReverseGeo;
import com.iot.learnings.reverse.geo.service.ReverseGeoService;

@RestController
@RequestMapping("/api/v1/reversegeo")
public class ReverseGeoController {

	private static final Logger LOG = LoggerFactory.getLogger(ReverseGeoController.class);

	@Autowired
	ReverseGeoService reverseGeoService;

	@Autowired
	ReverseGeoMapper reverseGeoMapper;

	@PostMapping("/revgeo")
	public ResponseEntity<ReverseGeoResponseDTO> retrieveByLatLon(
			@RequestBody ReverseGeoRequestDTO reverseGeoRequestDTO) {
		LOG.info("GetLatLon => Lat:{}, Lon: {}", reverseGeoRequestDTO.getLat(), reverseGeoRequestDTO.getLon());
		ReverseGeo request = reverseGeoMapper.mapDTOToDomain(reverseGeoRequestDTO);
		Optional<ReverseGeo> response = reverseGeoService.findByLatitudeAndLongitude(request.getLat(),
				request.getLon());
		if (response.isPresent()) {
			return ResponseEntity.ok(reverseGeoMapper.mapDomainToDTO(response.get()));
		}
		return new ResponseEntity<ReverseGeoResponseDTO>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/revgeo/{lat}/{lon}")
	public ResponseEntity<ReverseGeoResponseDTO> getByLatLon(@PathVariable Double lat, @PathVariable Double lon) {
		LOG.info("GetLatLon => Lat:{}, Lon: {}", lat, lon);
		Optional<ReverseGeo> response = reverseGeoService.findByLatitudeAndLongitude(lat, lon);
		if (response.isPresent()) {
			return ResponseEntity.ok(reverseGeoMapper.mapDomainToDTO(response.get()));
		}
		return new ResponseEntity<ReverseGeoResponseDTO>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/revgeo")
	public ResponseEntity<List<ReverseGeoResponseDTO>> getByLatLon() {
		List<ReverseGeo> reveseGeoList = reverseGeoService.findAll();
		LOG.info("GetLatLon => Lat:{}, Lon: {}", 35.6897, 139.6922);
		List<ReverseGeoResponseDTO> dtos = reveseGeoList.stream().map(x -> reverseGeoMapper.mapDomainToDTO(x))
				.collect(Collectors.toList());
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}
