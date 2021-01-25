package com.iot.learnings.socket.worker;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;

import com.iot.learnings.event.Event;
import com.iot.learnings.socket.exception.WorkerClosedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class IotWorker implements Worker {

	protected String connectionID;
	protected Socket socket;
	protected KafkaTemplate<String, Event> iotEventKafkaTemplate;
	protected int delayBetweenPacketsInMillis;
	protected volatile boolean running;

	public IotWorker(String connectionID, Socket socket, KafkaTemplate<String, Event> iotEventKafkaTemplate,
			int delayBetweenPacketsInMillis) {
		this.socket = socket;
		this.iotEventKafkaTemplate = iotEventKafkaTemplate;
		this.delayBetweenPacketsInMillis = delayBetweenPacketsInMillis;
		this.connectionID = connectionID;
	}

	@Override
	public void run() {
		try {
			List<Integer> packets = new LinkedList<Integer>();
			int zeroPacketCount = 0;
			while (running) {
				int packet = this.socket.getInputStream().read();
				if (packet != -1) {
					zeroPacketCount = 0;
					packets.add(packet);
					if (isPacketCompete(packets)) {
						processPacket(packets, socket);
						packets.clear();
						applyDelayBetweenPackets();
					}
				} else {
					zeroPacketCount += 1;
					applyDelayBetweenPackets();
				}

				if (zeroPacketCount > 10) {
					// close the connection
				}
			}
		} catch (WorkerClosedException wce) {

		} catch (Exception e) {
			log.error("Exception while receiving the incoming packets", e);
		} finally {
			closeSocketQuitely(socket);
		}
	}

	protected abstract boolean isPacketCompete(List<Integer> packets);

	protected abstract void processPacket(List<Integer> packets, Socket socket);

	public void applyDelayBetweenPackets() {
		try {
			TimeUnit.MILLISECONDS.sleep(delayBetweenPacketsInMillis);
		} catch (Exception e) {
		}
	}

	@Override
	public void start() {
		running = true;
	}

	@Override
	public void stop() {
		running = false;
	}

	@Override
	public String getConnectionID() {
		return connectionID;
	}

}
