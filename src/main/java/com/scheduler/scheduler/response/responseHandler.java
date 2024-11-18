package com.scheduler.scheduler.response;

import java.util.HashMap;
import java.util.Map;

public class responseHandler {

    public static Map<String, Object> createResponse(String status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return response;
    }
}
