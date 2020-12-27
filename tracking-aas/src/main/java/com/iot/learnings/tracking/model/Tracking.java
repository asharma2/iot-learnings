package com.iot.learnings.tracking.model;

import java.time.LocalDate;

import com.iot.learnings.model.Direction;

import lombok.Data;

@Data
public class Tracking {

	Long id;
	String imei;
	Double lat;
	Double lon;
	Double speed;
	Direction direction;
	LocalDate eventDate;

}
