package com.iot.learnings.reverse.geo.model;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.Data;

@Data
public class ReverseGeo implements IdentifiedDataSerializable {

	Long id;
	Double lat;
	Double lon;
	String city;
	String country;
	Double population;

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeLong(id);
		out.writeDouble(lat);
		out.writeDouble(lon);
		out.writeUTF(city);
		out.writeUTF(country);
		out.writeDouble(population);
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.id = in.readLong();
		this.lat = in.readDouble();
		this.lon = in.readDouble();
		this.city = in.readUTF();
		this.country = in.readUTF();
		this.population = in.readDouble();
	}

	@Override
	public int getFactoryId() {
		return ReverseGeoSerializableFactory.ID;
	}

	@Override
	public int getId() {
		return ReverseGeoSerializableFactory.REVERSE_GEO_ID;
	}

}
