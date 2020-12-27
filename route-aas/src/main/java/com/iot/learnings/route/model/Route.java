package com.iot.learnings.route.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Route {

	Long id;
	String username;
	String vehicleNo;
	String imei;
	Double lat;
	Double lon;
	Double speed;
	String city;
	LocalDateTime eventDate;
}
