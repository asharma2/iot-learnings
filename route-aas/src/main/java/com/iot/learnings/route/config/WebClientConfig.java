package com.iot.learnings.route.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
@Slf4j
public class WebClientConfig {

	@Value("${reverse.geo.base-url}")
	String reverseGeoBaseUrl;

	@Bean
	public WebClient revGeoWebClient() {
		TcpClient tcpClient = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				});
		return WebClient.builder().baseUrl(reverseGeoBaseUrl).filter(logRequest()).filter(logResponse())
				.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			log.info("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
			return Mono.just(clientResponse);
		});
	}
}
