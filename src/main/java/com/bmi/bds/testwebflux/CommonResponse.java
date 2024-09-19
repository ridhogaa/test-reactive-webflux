package com.bmi.bds.testwebflux;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonResponse {
    private List<Map<String, Object>> result;
    private String responseCode;
}
