package com.iot.learnings.socket.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

	@Value("${io.server.thread.pool:1}")
	Integer iotServerThreadPool;

	@Bean("iotServerThreadPool")
	public ExecutorService iotServerThreadPool() {
		return Executors.newFixedThreadPool(iotServerThreadPool);
	}

	@Bean("iotWorkerThreadPool")
	public ExecutorService iotWorkerThreadPool() {
		return Executors.newWorkStealingPool();
	}
}
