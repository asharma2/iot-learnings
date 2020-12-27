package com.iot.learnings.driver.service;

import java.util.Optional;

import com.iot.learnings.driver.model.Driver;

public interface DriverService {

	void save(Driver driver);

	Optional<Driver> findByUsername(String username);

	Optional<Driver> findByVehicleNo(String vehicleNo);
}
