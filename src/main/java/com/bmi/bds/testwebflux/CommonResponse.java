package com.bmi.bds.testwebflux;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonResponse<T> {
    private T result;
    private String responseCode;
}
