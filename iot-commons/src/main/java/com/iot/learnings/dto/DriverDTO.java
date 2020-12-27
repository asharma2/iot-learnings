package com.iot.learnings.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverDTO extends TrackingDTO {

	String username;
	String vehicleNo;
}
