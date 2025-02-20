package com.anderson.users.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class MetadataResponse {
    private HashMap<String, String> metadata = new HashMap<>();

    public void setMetadata(String status, String message) {
        metadata.put("status", status);
        metadata.put("message", message);
    }
}
