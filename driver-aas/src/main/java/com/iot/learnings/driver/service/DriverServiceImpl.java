package com.iot.learnings.driver.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iot.learnings.driver.model.Driver;

@Service
public class DriverServiceImpl implements DriverService {

	private static final Logger LOG = LoggerFactory.getLogger(DriverServiceImpl.class);

	Map<String, Driver> driverByUsername = new HashMap<>();
	Map<String, Driver> driverByVehicleNo = new HashMap<>();
	AtomicLong seq = new AtomicLong(1);

	@Override
	public void save(Driver driver) {
		if (!driverByVehicleNo.containsKey(driver.getVehicleNo())) {
			driver.setId(seq.incrementAndGet());
			driver.setEmail("alexjr" + driver.getId() + "@gmail.com");
			driver.setUsername("alexjr" + driver.getId());
			driverByUsername.put(driver.getUsername(), driver);
			driverByVehicleNo.put(driver.getVehicleNo(), driver);
		}
	}

	@Override
	public Optional<Driver> findByUsername(String username) {
		LOG.info("DriverByUsername => Username: {}", username);
		if (!driverByUsername.containsKey(username)) {
			return Optional.empty();
		}
		return Optional.of(driverByUsername.get(username));
	}

	@Override
	public Optional<Driver> findByVehicleNo(String vehicleNo) {
		LOG.info("DriverByVehicleNo => VehicleNo: {}", vehicleNo);
		if (!driverByVehicleNo.containsKey(vehicleNo)) {
			return Optional.empty();
		}
		return Optional.of(driverByVehicleNo.get(vehicleNo));
	}

}
