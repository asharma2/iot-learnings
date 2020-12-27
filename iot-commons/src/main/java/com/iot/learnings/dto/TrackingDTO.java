package com.iot.learnings.dto;

import com.iot.learnings.model.Direction;

import lombok.Data;

@Data
public class TrackingDTO implements DTO {

	String imei;
	Double lat;
	Double lon;
	Double speed;
	Direction direction;
}
