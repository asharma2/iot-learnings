package com.iot.learnings.alert.model;

import java.time.LocalDateTime;

import com.iot.learnings.model.AlertMetadata;
import com.iot.learnings.model.AlertType;
import com.iot.learnings.model.Severity;

import lombok.Data;

@Data
public class Alert {

	Long id;
	Severity severity;
	AlertType alertType;
	AlertMetadata metadata;
	LocalDateTime alertDate;
}
