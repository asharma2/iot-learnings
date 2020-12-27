package com.iot.learnings.tracking.service;

import java.util.List;
import java.util.Optional;

import com.iot.learnings.tracking.model.Tracking;

public interface TrackingService {

	Tracking save(Tracking tracking);

	Optional<Tracking> findLatestByImei(String imei);

	List<Tracking> findByImei(String imei);

}
