package com.iot.learnings.tracking.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.iot.learnings.tracking.model.Tracking;

@Service
public class TrackingServiceImpl implements TrackingService {

	Map<String, List<Tracking>> imeiTracking = new HashMap<>();
	AtomicLong seq = new AtomicLong(0);

	@Override
	public Tracking save(Tracking tracking) {
		if (!imeiTracking.containsKey(tracking.getImei())) {
			imeiTracking.put(tracking.getImei(), new LinkedList<>());
		}
		tracking.setId(seq.incrementAndGet());
		imeiTracking.get(tracking.getImei()).add(tracking);
		return tracking;
	}

	@Override
	public Optional<Tracking> findLatestByImei(String imei) {
		if (!imeiTracking.containsKey(imei)) {
			return Optional.empty();
		}
		return Optional.of(imeiTracking.get(imei).get(0));
	}

	@Override
	public List<Tracking> findByImei(String imei) {
		return (imeiTracking.containsKey(imei)) ? imeiTracking.get(imei) : Collections.emptyList();
	}
}
