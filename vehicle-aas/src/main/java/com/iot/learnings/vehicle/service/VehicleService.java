package com.iot.learnings.vehicle.service;

import java.util.Optional;

import com.iot.learnings.vehicle.model.Vehicle;

public interface VehicleService {

	void save(Vehicle vehicle);

	Optional<Vehicle> findByImei(String imei);

	Optional<Vehicle> findByVehicleNo(String vehicleNo);
}
