package com.iot.learnings.socket.container;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.iot.learnings.socket.server.Server;
import com.iot.learnings.socket.worker.Worker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IotContainer implements Container<Server<Worker>> {

	private List<Server<? extends Worker>> servers;
	private ExecutorService iotServerExecutorService;

	public IotContainer(List<Server<? extends Worker>> servers, ExecutorService iotServerExecutorService) {
		this.servers = servers;
		this.iotServerExecutorService = iotServerExecutorService;
	}

	@Override
	public void start() {
		for (Server<? extends Worker> server : servers) {
			server.start();
			iotServerExecutorService.submit(server);
			log.info("Started server on port: {}", server.getPort());
		}
	}

	@Override
	public void stop() {
		for (Server<? extends Worker> server : servers) {
			server.stop();
			log.info("Stopped server on port: {}", server.getPort());
		}
	}

}
