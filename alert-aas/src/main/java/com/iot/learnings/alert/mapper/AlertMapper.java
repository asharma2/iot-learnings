package com.iot.learnings.alert.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.iot.learnings.alert.model.Alert;
import com.iot.learnings.dto.AlertDTO;
import com.iot.learnings.event.AlertEvent;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AlertMapper {

	default AlertEvent mapDomainToEvent(Alert alert) {
		AlertEvent event = new AlertEvent();
		event.setAlertType(alert.getAlertType());
		event.setMetadata(alert.getMetadata());
		event.setSeverity(alert.getSeverity());
		return event;
	}

	default AlertDTO mapDomainToDTO(Alert alert) {
		AlertDTO dto = new AlertDTO();
		dto.setAlertType(alert.getAlertType());
		dto.setMetadata(alert.getMetadata());
		dto.setSeverity(alert.getSeverity());
		dto.setAlertDate(alert.getAlertDate());
		return dto;
	}

}
