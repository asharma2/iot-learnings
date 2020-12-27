package com.iot.learnings.alert.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iot.learnings.alert.model.Alert;
import com.iot.learnings.model.Severity;

@Service
public class AlertServiceImpl implements AlertService {

	Map<Severity, List<Alert>> alertBySeverity = new HashMap<>();
	AtomicLong seq = new AtomicLong(1);

	@Override
	public void save(Alert alert) {
		if (!alertBySeverity.containsKey(alert.getSeverity())) {
			alertBySeverity.put(alert.getSeverity(), new LinkedList<>());
		}
		alert.setId(seq.incrementAndGet());
		alertBySeverity.get(alert.getSeverity()).add(alert);
	}

	@Override
	public List<Alert> findBySeverity(Severity severity) {
		if (!alertBySeverity.containsKey(severity)) {
			return Collections.emptyList();
		}
		return alertBySeverity.get(severity);
	}

	@Override
	public List<Alert> findAll() {
		return alertBySeverity.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList());
	}

}
