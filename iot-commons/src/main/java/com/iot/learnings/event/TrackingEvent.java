package com.iot.learnings.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.learnings.model.Direction;

import lombok.Data;

@Data
public class TrackingEvent implements Event {

	String imei;
	Double lat;
	Double lon;
	Double speed;
	Direction direction;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime eventDate;

}
