package com.iot.learnings.reverse.geo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.iot.learnings.reverse.geo.model.GeoQuery;
import com.iot.learnings.reverse.geo.model.ReverseGeo;

@Service
public class ReverseGeoServiceImpl implements ReverseGeoService {

	@Autowired
	HazelcastInstance hazelcastInstance;
	AtomicLong seq = new AtomicLong(1);

	@Override
	public Optional<ReverseGeo> findByLatitudeAndLongitude(double lat, double lon) {
		IMap<GeoQuery, ReverseGeo> revGeo = hazelcastInstance.getMap("revgeo");
		return Optional.ofNullable(revGeo.get(new GeoQuery(lat, lon)));
	}

	@Override
	public ReverseGeo save(ReverseGeo reverseGeo) {
		IMap<GeoQuery, ReverseGeo> revGeo = hazelcastInstance.getMap("revgeo");
		GeoQuery geoQuery = new GeoQuery(reverseGeo.getLat(), reverseGeo.getLon());
		if (!revGeo.containsKey(geoQuery)) {
			reverseGeo.setId(seq.get());
			revGeo.put(geoQuery, reverseGeo);
		}
		return reverseGeo;
	}

	@Override
	public void delete(ReverseGeo reverseGeo) {
		IMap<GeoQuery, ReverseGeo> revGeo = hazelcastInstance.getMap("revgeo");
		revGeo.tryRemove(new GeoQuery(reverseGeo.getLat(), reverseGeo.getLon()), 5L, TimeUnit.SECONDS);
	}

	@Override
	public List<ReverseGeo> findAll() {
		IMap<GeoQuery, ReverseGeo> revGeo = hazelcastInstance.getMap("revgeo");
		return new ArrayList<>(revGeo.values());
	}

}
