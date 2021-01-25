package com.iot.learnings.socket.server;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.springframework.kafka.core.KafkaTemplate;

import com.iot.learnings.event.Event;
import com.iot.learnings.socket.worker.ConcoxWorker;
import com.iot.learnings.socket.worker.Worker;

public class ConcoxServer extends IotServer<ConcoxWorker> {

	public ConcoxServer(int port, ExecutorService iotWorkerExecutorService,
			KafkaTemplate<String, Event> iotEventKafkaTemplate) {
		super(port, iotWorkerExecutorService, iotEventKafkaTemplate);
	}

	@Override
	protected Worker iotWorker(Socket socket) {
		return new ConcoxWorker(UUID.randomUUID().toString(), socket, iotEventKafkaTemplate, 1000);
	}

}
