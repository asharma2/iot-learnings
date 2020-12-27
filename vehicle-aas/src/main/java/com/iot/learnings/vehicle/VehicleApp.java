package com.iot.learnings.vehicle;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.iot.learnings.vehicle.model.Vehicle;
import com.iot.learnings.vehicle.service.VehicleService;

@SpringBootApplication
public class VehicleApp implements ApplicationRunner {

	@Autowired
	VehicleService vehicleService;

	public static void main(String[] args) {
		SpringApplication.run(VehicleApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> imeis = Arrays.asList("333535165171631", "911870400372672", "453124255522351", "442729442005633",
				"522893802227105", "018015435544307");
		for (String imei : imeis) {
			Vehicle vehicle = new Vehicle();
			vehicle.setImei(imei);
			vehicleService.save(vehicle);
		}
	}
}
