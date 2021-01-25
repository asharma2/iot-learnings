package com.iot.learnings.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimulatorConfig {

	@Bean
	@ConfigurationProperties(prefix = "simulator.properties.concox")
	SimulatorProperties concoxSimulatorProperties() {
		return new SimulatorProperties();
	}
}
