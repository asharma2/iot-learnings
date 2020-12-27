package com.iot.learnings.device.model;

import com.iot.learnings.model.DeviceType;

import lombok.Data;

@Data
public class Device {

	Long id;
	String imei;
	Integer made;
	DeviceType deviceType;
}
