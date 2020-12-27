package com.iot.learnings.dto;

import java.time.LocalDateTime;

import com.iot.learnings.model.AlertMetadata;
import com.iot.learnings.model.AlertType;
import com.iot.learnings.model.Severity;

import lombok.Data;

@Data
public class AlertDTO implements DTO {

	Severity severity;
	AlertType alertType;
	AlertMetadata metadata;
	LocalDateTime alertDate;
}
