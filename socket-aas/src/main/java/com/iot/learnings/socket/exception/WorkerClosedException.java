package com.iot.learnings.socket.exception;

public class WorkerClosedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WorkerClosedException(Throwable cause) {
		super(cause);
	}
}
