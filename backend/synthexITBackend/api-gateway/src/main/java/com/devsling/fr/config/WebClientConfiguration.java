package com.devsling.fr.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WebClientConfiguration {
    @Value("${backend.maxConnections:600}")
    private int maxConnections;
    @Value("${backend.acquireTimeout:1000}")
    private int acquireTimeout;
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    private String baseUrl;
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;
    private int responseTimeout;

    protected WebClient.Builder webClientBuilder(String service, WebClient.Builder webClientBuilder,
                                                 List<? extends ExchangeFilterFunction> requestFilters) {

        WebClient.Builder builder = webClientBuilder
                .baseUrl(getBaseUrl())
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1))
                .clientConnector(httpClientConnector())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        requestFilters.forEach(builder::filter);
        return builder;
    }

    protected ClientHttpConnector httpClientConnector() {
        String connectionProviderName = "fixed";
        return new ReactorClientHttpConnector(
                HttpClient.create(
                                ConnectionProvider.builder(connectionProviderName)
                                        .maxConnections(maxConnections)
                                        .pendingAcquireTimeout(Duration.ofMillis(acquireTimeout))
                                        .build())
                        .responseTimeout(responseTimeout == 0 ? null : Duration.ofMillis(responseTimeout))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout) // millis
                        .doOnConnected(connection ->
                                connection
                                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)))
                        .wiretap(this.getClass().getCanonicalName(),
                                LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
        );
    }
}
