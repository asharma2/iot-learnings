package com.iot.learnings.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleDTO extends TrackingDTO {

	String vehicleNo;
	String registrationNo;
	String model;
	Integer made;
}
