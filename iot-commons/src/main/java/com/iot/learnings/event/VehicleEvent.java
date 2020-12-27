package com.iot.learnings.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleEvent extends TrackingEvent {

	String vehicleNo;
	String registrationNo;
	String model;
	Integer made;
}
