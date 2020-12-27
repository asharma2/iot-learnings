package com.iot.learnings.reverse.geo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GeoQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	Double lat;
	Double lon;
}
