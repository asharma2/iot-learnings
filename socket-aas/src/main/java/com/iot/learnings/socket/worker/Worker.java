package com.iot.learnings.socket.worker;

import java.net.Socket;

import com.iot.learnings.socket.exception.IotWorkerException;

public interface Worker extends Runnable {

	void start();

	void stop();

	default void closeSocketQuitely(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			throw new IotWorkerException(e);
		}
	}

	String getConnectionID();
}
