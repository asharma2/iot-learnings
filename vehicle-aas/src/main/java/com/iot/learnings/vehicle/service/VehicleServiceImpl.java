package com.iot.learnings.vehicle.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iot.learnings.vehicle.model.Vehicle;

@Service
public class VehicleServiceImpl implements VehicleService {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleServiceImpl.class);

	Map<String, Vehicle> vehicleByImei = new HashMap<>();
	Map<String, Vehicle> vehicleByVehicleNo = new HashMap<>();
	AtomicLong seq = new AtomicLong(1);

	@Override
	public void save(Vehicle vehicle) {
		if (!vehicleByImei.containsKey(vehicle.getImei())) {
			vehicleByImei.put(vehicle.getImei(), vehicle);
			vehicleByVehicleNo.put(vehicle.getVehicleNo(), vehicle);
			vehicle.setId(seq.incrementAndGet());
			vehicle.setImei(vehicle.getImei());
			vehicle.setMade(2020);
			vehicle.setModel("Maruti");
			vehicle.setRegistrationNo(vehicle.getImei());
			vehicle.setVehicleNo("DL2CAW908" + vehicle.getId());
		}
	}

	@Override
	public Optional<Vehicle> findByImei(String imei) {
		LOG.info("VehicleByImei => Imei: {}", imei);
		if (!vehicleByImei.containsKey(imei)) {
			return Optional.empty();
		}
		return Optional.of(vehicleByImei.get(imei));
	}

	@Override
	public Optional<Vehicle> findByVehicleNo(String vehicleNo) {
		LOG.info("VehicleByVehicleNo => VehicleNo: {}", vehicleNo);
		if (!vehicleByVehicleNo.containsKey(vehicleNo)) {
			return Optional.empty();
		}
		return Optional.of(vehicleByVehicleNo.get(vehicleNo));
	}

}
