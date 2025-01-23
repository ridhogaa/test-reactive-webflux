package com.bmi.bds.testwebflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@BaseController(value = "/bulk")
@RequiredArgsConstructor
public class BulkController {

    private final BulkService dataProcessingService;

    @GetMapping()
    public Object processData() {
        List<String> dataList = Arrays.asList("Data-1", "Data-2", "Data-3", "Data-4", "Data-5",
                "Data-6", "Data-7", "Data-8", "Data-9", "Data-10");

        return dataProcessingService.processAndSendData(dataList);

    }
}
