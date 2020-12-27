package com.iot.learnings.event;

import com.iot.learnings.model.AlertMetadata;
import com.iot.learnings.model.AlertType;
import com.iot.learnings.model.Severity;

import lombok.Data;

@Data
public class AlertEvent implements Event {

	AlertType alertType;
	AlertMetadata metadata;
	Severity severity;
}
