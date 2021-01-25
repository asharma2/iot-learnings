package com.iot.learnings.socket.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "socket.properties")
public class SocketProperties {

	List<Integer> ports;
	Integer delayBetweenPackets;
	Integer maxConnections;

}
