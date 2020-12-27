package com.iot.learnings.alert.service;

import java.util.List;

import com.iot.learnings.alert.model.Alert;
import com.iot.learnings.model.Severity;

public interface AlertService {

	void save(Alert alert);

	List<Alert> findBySeverity(Severity severity);

	List<Alert> findAll();
}
