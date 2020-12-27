package com.iot.learnings.route.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.iot.learnings.route.model.Route;

@Service
public class RouteServiceImpl implements RouteService {

	Map<String, Route> routeByVehicleNo = new HashMap<>();
	AtomicLong seq = new AtomicLong(1);

	@Override
	public void save(Route route) {
		route.setId(seq.incrementAndGet());
		routeByVehicleNo.put(route.getVehicleNo(), route);
	}

	@Override
	public Optional<Route> findByVehicleNo(String vehicleNo) {
		if (!routeByVehicleNo.containsKey(vehicleNo)) {
			return Optional.empty();
		}
		return Optional.of(routeByVehicleNo.get(vehicleNo));
	}

	@Override
	public List<Route> findAll() {
		return new ArrayList<>(routeByVehicleNo.values());
	}

}
