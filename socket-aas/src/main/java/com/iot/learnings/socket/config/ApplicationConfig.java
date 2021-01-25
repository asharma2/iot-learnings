package com.iot.learnings.socket.config;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import com.iot.learnings.event.Event;
import com.iot.learnings.socket.container.IotContainer;
import com.iot.learnings.socket.model.SocketProperties;
import com.iot.learnings.socket.server.ConcoxServer;
import com.iot.learnings.socket.server.Server;
import com.iot.learnings.socket.worker.Worker;

@Configuration
public class ApplicationConfig {

	@Bean
	@ConfigurationProperties(prefix = "socket.concox.properties")
	public SocketProperties socketConcoxProperties() {
		return new SocketProperties();
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public IotContainer iotContainer(SocketProperties socketConcoxProperties,
			@Qualifier("iotServerThreadPool") ExecutorService iotServerThreadPool,
			@Qualifier("iotWorkerThreadPool") ExecutorService iotWorkerThreadPool,
			KafkaTemplate<String, Event> iotEventKafkaTemplate) {
		List<Server<? extends Worker>> servers = new LinkedList<>();
		for (Integer port : socketConcoxProperties.getPorts()) {
			servers.add(new ConcoxServer(port, iotWorkerThreadPool, iotEventKafkaTemplate));
		}
		return new IotContainer(servers, iotServerThreadPool);
	}
}
