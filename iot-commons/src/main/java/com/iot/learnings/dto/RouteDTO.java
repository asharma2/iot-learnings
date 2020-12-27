package com.iot.learnings.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RouteDTO implements DTO {

	String username;
	String vehicleNo;
	String imei;
	Double lat;
	Double lon;
	Double speed;
	String city;
	LocalDate eventDate;
}
