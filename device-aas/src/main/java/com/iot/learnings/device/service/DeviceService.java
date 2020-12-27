package com.iot.learnings.device.service;

import java.util.Optional;

import com.iot.learnings.device.model.Device;

public interface DeviceService {

	void save(Device device);

	Optional<Device> findByImei(String imei);

}
