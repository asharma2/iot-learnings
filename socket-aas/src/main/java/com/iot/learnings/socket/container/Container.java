package com.iot.learnings.socket.container;

import com.iot.learnings.socket.server.Server;
import com.iot.learnings.socket.worker.Worker;

public interface Container<T extends Server<Worker>> {

	void start();

	void stop();
}
