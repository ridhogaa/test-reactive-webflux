package com.bmi.bds.testwebflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkService {

    private final PostRestClientReactive postRestClientReactive;

    public List<Map<String, Object>> processAndSendData(List<String> dataList) {
        // Step 1: Create a thread pool for parallel execution
        int threadCount = 10; // Adjust thread count based on system needs
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // Step 2: Convert data to CompletableFutures
        List<CompletableFuture<Map<String, Object>>> futures = dataList.stream()
                .map(data -> CompletableFuture
                        .supplyAsync(() -> sendToExternalService(data), executorService))
                .toList();

        // Step 3: Collect all responses
        List<Map<String, Object>> responses = futures.stream()
                .map(CompletableFuture::join) // Wait for each future to complete
                .toList();

        // Step 4: Shutdown thread pool
        executorService.shutdown();

        return responses; // Return the collected responses
    }

    private Map<String, Object> sendToExternalService(String data) {
        // Example endpoint URL (replace with your actual endpoint)
        String url = "https://jsonplaceholder.typicode.com/users";
        log.info("Data: {}", data);
        return postRestClientReactive.invokeAsync(url, data).join();
    }
}
