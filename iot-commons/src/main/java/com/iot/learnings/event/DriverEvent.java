package com.iot.learnings.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class DriverEvent extends TrackingEvent {

	String username;
	String vehicleNo;
}
