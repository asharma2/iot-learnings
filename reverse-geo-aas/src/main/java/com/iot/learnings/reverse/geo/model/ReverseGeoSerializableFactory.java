package com.iot.learnings.reverse.geo.model;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class ReverseGeoSerializableFactory implements DataSerializableFactory {

	public static final int ID = 1;
	public static final int REVERSE_GEO_ID = 10;

	@Override
	public IdentifiedDataSerializable create(int typeId) {
		if (typeId == REVERSE_GEO_ID)
			return new ReverseGeo();
		return null;
	}

}
