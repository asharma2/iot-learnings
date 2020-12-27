package com.iot.learnings.model;

public enum Direction {
	NE("NorthEast"), //
	SE("SouthEast"), //
	SW("SouthWest"), //
	NW("NorthWest"); //

	private String direction;

	private Direction(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}
}
