package com.iot.learnings.simulator.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "simulator.properties")
public class SimulatorProperties {

	String hostname;
	List<Integer> ports;
	Integer delayBetweenPackets;
	Integer maxConnections;
	String imeiGeneration;
	String repeatPath;
	String repeatType;

}
