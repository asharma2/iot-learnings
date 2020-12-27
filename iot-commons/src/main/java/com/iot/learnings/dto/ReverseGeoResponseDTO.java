package com.iot.learnings.dto;

import lombok.Data;

@Data
public class ReverseGeoResponseDTO implements DTO {

	Double lat;
	Double lon;
	String city;
	String state;
	Double population;
}
