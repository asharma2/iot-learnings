package com.iot.learnings.device;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.iot.learnings.device.model.Device;
import com.iot.learnings.device.service.DeviceService;
import com.iot.learnings.model.DeviceType;

@SpringBootApplication
public class DeviceApp implements ApplicationRunner {

	@Autowired
	DeviceService deviceService;

	public static void main(String[] args) {
		SpringApplication.run(DeviceApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Device> devices = Arrays.asList(createDevice("333535165171631", DeviceType.Concox, 2009),
				createDevice("911870400372672", DeviceType.Concox, 2010),
				createDevice("453124255522351", DeviceType.Coban, 2011),
				createDevice("442729442005633", DeviceType.Coban, 2012),
				createDevice("522893802227105", DeviceType.Coban, 2013),
				createDevice("018015435544307", DeviceType.Concox, 2014));
		for (Device device : devices) {
			deviceService.save(device);
		}
	}

	private Device createDevice(String imei, DeviceType deviceType, Integer made) {
		Device device = new Device();
		device.setImei(imei);
		device.setDeviceType(deviceType);
		device.setMade(made);
		return device;
	}
}
