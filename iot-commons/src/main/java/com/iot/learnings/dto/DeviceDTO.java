package com.iot.learnings.dto;

import com.iot.learnings.model.Connectivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceDTO extends TrackingDTO {

	boolean ignition;
	Connectivity connectivity;
}
