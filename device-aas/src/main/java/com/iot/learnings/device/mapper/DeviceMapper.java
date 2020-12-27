package com.iot.learnings.device.mapper;

import org.mapstruct.Mapper;

import com.iot.learnings.device.model.Device;
import com.iot.learnings.event.DeviceEvent;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

	Device mapEventToDomain(DeviceEvent deviceEvent);
}
