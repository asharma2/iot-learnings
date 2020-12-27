package com.iot.learnings.reverse.geo.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReverseGeoCsvData {

	String city;
	Double lat;
	Double lon;
	String country;
	String iso2;
	String iso3;
	BigDecimal population;
}
