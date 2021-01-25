package com.iot.learnings.socket.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

import org.springframework.kafka.core.KafkaTemplate;

import com.iot.learnings.event.Event;
import com.iot.learnings.socket.exception.IotServerException;
import com.iot.learnings.socket.worker.Worker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class IotServer<W extends Worker> implements Server<W> {

	protected int port;
	protected ExecutorService iotWorkerExecutorService;
	protected KafkaTemplate<String, Event> iotEventKafkaTemplate;
	protected ServerSocket serverSocket;
	protected List<W> workers;
	protected ConcurrentMap<String, Worker> connections = new ConcurrentHashMap<String, Worker>();
	/**
	 * 
	 */
	protected volatile boolean running = false;

	public IotServer(int port, ExecutorService iotWorkerExecutorService,
			KafkaTemplate<String, Event> iotEventKafkaTemplate) {
		super();
		this.port = port;
		this.iotWorkerExecutorService = iotWorkerExecutorService;
		this.iotEventKafkaTemplate = iotEventKafkaTemplate;
	}

	@Override
	public void run() {
		try {
			while (running) {
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				socket.setReuseAddress(true);
				socket.setSoTimeout(0);
				Worker worker = iotWorker(socket);
				worker.start();
				iotWorkerExecutorService.submit(worker);
				connections.put(worker.getConnectionID(), worker);
			}
		} catch (Exception e) {
			log.error("Exception while accepting the client connection", e);
		}
	}

	protected abstract Worker iotWorker(Socket socket);

	@Override
	public void start() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.serverSocket.setSoTimeout(0);
			this.running = true;
		} catch (Exception e) {
			throw new IotServerException(e);
		}
	}

	@Override
	public void stop() {
		for (W worker : workers) {
			worker.stop();
		}
		running = false;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void closeWorker(String connectionID) {
		if (connections.containsKey(connectionID)) {
			long s = System.currentTimeMillis();
			log.info("Going to close the connectionID: {}", connectionID);
			connections.get(connectionID).stop();
			log.info("ConnectionID: {} closed. Time: {}", System.currentTimeMillis() - s);
		}
	}

}
