package com.iot.learnings.simulator.worker;

import java.util.concurrent.TimeUnit;

public interface SimulatorWorker extends Runnable {

	default void delayInMillis(Integer delay) {
		try {
			TimeUnit.MILLISECONDS.sleep(delay);
		} catch (Exception e) {
		}
	}
	
	void stop();
}
