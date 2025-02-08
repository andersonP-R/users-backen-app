package com.anderson.users.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class MetadataResponse {
    private ArrayList<HashMap<String, String>> metadata = new ArrayList<>();

    public void setMetadata(String status, String message) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("status", status);
        map.put("message", message);
        metadata.add(map);
    }
}
