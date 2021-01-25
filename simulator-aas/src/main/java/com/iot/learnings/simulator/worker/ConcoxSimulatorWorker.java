package com.iot.learnings.simulator.worker;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcoxSimulatorWorker implements SimulatorWorker {

	protected Socket socket;
	protected List<List<Integer>> packets;
	protected Integer delayedInMillis;
	protected CountDownLatch signal;
	protected boolean running = true;

	public ConcoxSimulatorWorker(Socket socket, List<List<Integer>> packets, Integer delayedInMillis,
			CountDownLatch signal) {
		this.socket = socket;
		this.packets = packets;
		this.delayedInMillis = delayedInMillis;
		this.signal = signal;
	}

	@Override
	public void run() {
		try {
			while (running) {
				for (List<Integer> packet : packets) {
					for (Integer p : packet) {
						socket.getOutputStream().write(p);
					}
					delayInMillis(delayedInMillis);
				}
			}
		} catch (Exception e) {
			log.error("Exception while writing the packet", e);
		} finally {
			signal.countDown();
		}

	}

	@Override
	public void stop() {
		running = false;
	}
}
