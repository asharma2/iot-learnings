package com.iot.learnings.tracking.mapper;

import org.mapstruct.Mapper;

import com.iot.learnings.dto.TrackingDTO;
import com.iot.learnings.event.TrackingEvent;
import com.iot.learnings.tracking.model.Tracking;

@Mapper(componentModel = "spring")
public interface TrackingMapper {

	Tracking mapDTOToDomain(TrackingDTO trackingDTO);

	TrackingDTO mapDomainToDTO(Tracking tracking);

	TrackingEvent mapDomainToEvent(Tracking tracking);
}
