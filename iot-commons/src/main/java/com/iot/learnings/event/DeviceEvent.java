package com.iot.learnings.event;

import com.iot.learnings.model.Connectivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceEvent extends TrackingEvent {

	boolean ignition;
	Connectivity connectivity;
}
