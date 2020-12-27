package com.iot.learnings.reverse.geo.service;

import java.util.List;
import java.util.Optional;

import com.iot.learnings.reverse.geo.model.ReverseGeo;

public interface ReverseGeoService {

	Optional<ReverseGeo> findByLatitudeAndLongitude(double lat, double lon);

	ReverseGeo save(ReverseGeo reverseGeo);

	void delete(ReverseGeo reverseGeo);
	
	List<ReverseGeo> findAll();
}
