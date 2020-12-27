package com.iot.learnings.vehicle.model;

import lombok.Data;

@Data
public class Vehicle {

	Long id;
	String imei;
	String vehicleNo;
	String registrationNo;
	String model;
	Integer made;

}
