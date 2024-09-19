package com.bmi.bds.testwebflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class PostRestClientReactive {

    private static final Logger log = LoggerFactory.getLogger(PostRestClientReactive.class);

    private final WebClient webClient;

    public PostRestClientReactive(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Map<String, Object>> invokeWithInternalHeader(String url, Object req) {
//        String authorizationHeader = "Bearer " + header;

        return this.webClient
                .method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> {
//                    httpHeaders.add(HttpHeaders.AUTHORIZATION, authorizationHeader);
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Object.class)
                .map(body -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("responseCode", "200");
                    resultMap.put("result", body);
                    log.info("SUCCESS {}", body);
                    return resultMap;
                })
                .onErrorResume(e -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("responseCode", "500");
                    log.error("ERROR {}", e.getMessage(), e);
                    return Mono.just(resultMap);
                });
    }

    public CompletableFuture<Map<String, Object>> invokeAsync(String url, Object req) {
        return invokeWithInternalHeader(url, req).toFuture();
    }
}
