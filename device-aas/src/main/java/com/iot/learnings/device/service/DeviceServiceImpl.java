package com.iot.learnings.device.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iot.learnings.device.model.Device;

@Service
public class DeviceServiceImpl implements DeviceService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

	Map<String, Device> devices = new HashMap<>();
	AtomicLong seq = new AtomicLong();

	@Override
	public void save(Device device) {
		if (!devices.containsKey(device.getImei())) {
			devices.put(device.getImei(), device);
			device.setId(seq.incrementAndGet());
		}
	}

	@Override
	public Optional<Device> findByImei(String imei) {
		LOG.info("DeviceByImei => Imei: {}", imei);
		if (!devices.containsKey(imei)) {
			return Optional.empty();
		}
		return Optional.of(devices.get(imei));
	}

}
