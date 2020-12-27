package com.iot.learnings.dto;

import lombok.Data;

@Data
public class ReverseGeoRequestDTO implements DTO {

	Double lat;
	Double lon;
}
