package com.iot.learnings.simulator;

import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.iot.learnings.simulator.config.SimulatorProperties;
import com.iot.learnings.simulator.converter.Converter;
import com.iot.learnings.simulator.worker.ConcoxSimulatorWorker;

@SpringBootApplication
public class SimulatorApp implements ApplicationRunner {

	@Autowired
	SimulatorProperties concoxSimulatorProperties;

	public static void main(String[] args) {
		SpringApplication.run(SimulatorApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Resource resource = new ClassPathResource("packets/concox.packet.log");
		List<String> files = Files.readAllLines(resource.getFile().toPath());
		String concoxPattern = "Convert the collection of byte to packet:";
		Converter<String, List<Integer>> concoxConverter = new Converter<String, List<Integer>>() {
			@Override
			public List<Integer> convert(String input) {
				int idx = input.indexOf(concoxPattern);
				if (idx != -1) {
					String actualPacket = input.substring(idx + concoxPattern.length() + 1).replaceAll("[\\[\\]]", "");
					String packetsAsText[] = actualPacket.split(",");
					List<Integer> packets = new ArrayList<Integer>(packetsAsText.length);
					for (String packet : packetsAsText) {
						packets.add(Integer.parseInt(packet.trim()));
					}
					return packets;
				}
				return Collections.emptyList();
			}
		};
		List<List<Integer>> packets = files.stream().map(x -> concoxConverter.convert(x)).collect(Collectors.toList());
		List<Integer> ports = concoxSimulatorProperties.getPorts();
		ExecutorService es = Executors.newFixedThreadPool(ports.size());
		CountDownLatch signal = new CountDownLatch(ports.size());
		for (Integer port : ports) {
			Socket socket = new Socket(concoxSimulatorProperties.getHostname(), port);
			ConcoxSimulatorWorker csw = new ConcoxSimulatorWorker(socket, packets,
					concoxSimulatorProperties.getDelayBetweenPackets(), signal);
			es.submit(csw);
		}
		signal.await();
		es.shutdown();
	}
}
