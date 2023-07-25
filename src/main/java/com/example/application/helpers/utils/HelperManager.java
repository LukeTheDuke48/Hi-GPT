package com.example.application.helpers.utils;

import com.example.application.helpers.IHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelperManager {
    private static HelperManager instance;
    private final List<Map<String, String>> messageHistory;
    private int totalTokens;

    private IHelper currentHelper;

    private HelperManager() {
        messageHistory = new ArrayList<>();
        totalTokens = 0;
        currentHelper = HelperFactory.createHelper(Helper.DAVE, null);
        messageHistory.add(0, Map.of("role", "system", "content", currentHelper.getSystemMessage()));
    }

    public static HelperManager getInstance() {
        if (instance == null) {
            instance = new HelperManager();
        }
        return instance;
    }

    public IHelper getCurrentHelper() {
        return currentHelper;
    }

    public void switchHelper(Helper helper, String customSystemMessage) {
        currentHelper = HelperFactory.createHelper(helper, customSystemMessage);
        // We'll assume that the first element of the message history will always be the system message :)
        if (!messageHistory.isEmpty()) {
            messageHistory.remove(0);
        }
        messageHistory.add(0, Map.of("role", "system", "content", currentHelper.getSystemMessage()));
    }

    public void resetMessageHistory() {
        messageHistory.clear();
        messageHistory.add(Map.of("role", "system", "content", currentHelper.getSystemMessage()));
    }

    public String getResponse(String message, String key, String apiVersion,
                                             int tokens, double temperature) {
        Pair<String, Integer> response = currentHelper.getResponse(message, key, apiVersion, messageHistory,
                tokens, temperature);
        totalTokens += response.getB();
        return response.getA();
    }

    public String mapToJson(Map<String, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getTotalTokens() {
        return totalTokens;
    }
}
