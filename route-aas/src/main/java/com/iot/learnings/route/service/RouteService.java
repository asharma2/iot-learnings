package com.iot.learnings.route.service;

import java.util.List;
import java.util.Optional;

import com.iot.learnings.route.model.Route;

public interface RouteService {

	void save(Route route);
	
	Optional<Route> findByVehicleNo(String vehicleNo);
	
	List<Route> findAll();
}
