package com.iot.learnings.socket.server;

import com.iot.learnings.socket.worker.Worker;

public interface Server<W extends Worker> extends Runnable {

	void start();

	void stop();

	int getPort();

	void closeWorker(String connectionID);
}
