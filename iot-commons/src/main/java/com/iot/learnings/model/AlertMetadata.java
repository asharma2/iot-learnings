package com.iot.learnings.model;

import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlertMetadata extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	public AlertMetadata() {
		super();
	}
}
