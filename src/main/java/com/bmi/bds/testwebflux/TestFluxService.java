package com.bmi.bds.testwebflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TestFluxService {

    @Autowired
    private PostRestClientReactive postRestClientReactive;

    public Object invoke() {
        log.info("Start call API");

        // Invoke the API
        Mono<Map<String, Object>> result = postRestClientReactive.invokeWithInternalHeader(
                "https://jsonplaceholder.typicode.com/users", ""
        );

        // Block and get the response
        Map<String, Object> resultMap = result.block();
        if (resultMap == null || resultMap.isEmpty()) {
            log.error("API response is null or empty!");
            return null;
        }

        log.info("Result Map ----> {}", resultMap);

        // Create a CommonResponse object
        CommonResponse<Object> response = new CommonResponse<>();
        response.setResponseCode((String) resultMap.get("responseCode"));

        // Map the result field dynamically
        Object resultData = resultMap.get("result");
        if (resultData instanceof List) {
            // Map to a List of desired type (e.g., List<User>)
            List<?> resultList = ((List<?>) resultData)
                    .stream()
                    .map(item -> new ModelMapper().map(item, Object.class))
                    .toList();
            response.setResult(resultList);
        } else {
            // Map to a single object
            response.setResult(new ModelMapper().map(resultData, Object.class));
        }

        log.info("Mapped Response ----> {}", writeToString(response));
        log.info("End call API");

        return response;
    }


    public static String writeToString(Object req) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
