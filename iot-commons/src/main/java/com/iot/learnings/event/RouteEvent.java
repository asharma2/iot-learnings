package com.iot.learnings.event;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RouteEvent implements Event {

	String imei;
	Double duration;
	Double distance;
	Double speed;
	LocalDate eventDate;
}
