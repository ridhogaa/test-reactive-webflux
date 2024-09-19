package com.bmi.bds.testwebflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class TestFluxService {

    @Autowired
    private PostRestClientReactive postRestClientReactive;

    public Object invoke() {
        log.info("start call api");
        Mono<Map<String, Object>> result = postRestClientReactive.invokeWithInternalHeader(
                "https://jsonplaceholder.typicode.com/users", ""
        );

        Map<String, Object> resultMap = result.block();
        log.info("result ----> {}", resultMap);

        ModelMapper modelMapper = new ModelMapper();
        CommonResponse<?> response = modelMapper.map(resultMap, CommonResponse.class);
        log.info("only result ----> {}", writeToString(response.getResult()));

        log.info("end call api");

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
