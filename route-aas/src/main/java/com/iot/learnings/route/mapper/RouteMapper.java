package com.iot.learnings.route.mapper;

import org.mapstruct.Mapper;

import com.iot.learnings.dto.RouteDTO;
import com.iot.learnings.event.RouteEvent;
import com.iot.learnings.route.model.Route;

@Mapper(componentModel = "spring")
public interface RouteMapper {

	RouteDTO mapDomainToDTO(Route route);

	RouteEvent mapDomainToEvent(Route route);
}
