package com.example.application.helpers;

import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Map;

public interface IHelper {
    public String getName();

    public String getSystemMessage();

    public Pair<String, Integer> getResponse(String message, String key, String apiVersion,
                                             List<Map<String, String>> messageHistory, int tokens, double temperature);
}
