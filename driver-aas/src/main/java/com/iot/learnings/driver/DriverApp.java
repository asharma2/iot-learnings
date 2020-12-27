package com.iot.learnings.driver;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.iot.learnings.driver.model.Driver;
import com.iot.learnings.driver.service.DriverService;

@SpringBootApplication
public class DriverApp implements ApplicationRunner {

	@Autowired
	DriverService driverService;

	public static void main(String[] args) {
		SpringApplication.run(DriverApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> vehicleNos = Arrays.asList("DL2CAW9081", "DL2CAW9082", "DL2CAW9083", "DL2CAW9084", "DL2CAW9085",
				"DL2CAW9086", "DL2CAW9087", "DL2CAW9088", "DL2CAW9089");
		for (String vehicleNo : vehicleNos) {
			Driver driver = new Driver();
			driver.setVehicleNo(vehicleNo);
			driverService.save(driver);
		}
	}
}
