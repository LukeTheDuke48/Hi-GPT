package com.example.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;

public class ChatGPTHelper {

    private List<Map<String, String>> messages;
    private int totalTokens = 0;


    public ChatGPTHelper() {
        // Initialize messages with a system message
        this.messages = new ArrayList<>();
        Map<String, String> systemMessage = Map.of("role", "system", "content", "You are a helpful assistant named Dave.");
        this.messages.add(systemMessage);
    }

    public String chatGPT(String message, String key, String apiVersion, int tokens, double temperature) {
        String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
        try {
        	OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");

            // Add the user's message to the history
            Map<String, String> userMessage = Map.of("role", "user", "content", message);
            this.messages.add(userMessage);

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("max_tokens", tokens);
            requestData.put("temperature", temperature);
            requestData.put("n", 1);
            requestData.put("model", apiVersion);
            requestData.put("messages", this.messages);

            RequestBody requestBody = RequestBody.create(mediaType, mapToJson(requestData));
            Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + key)
                .post(requestBody)
                .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                // Parsing the JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                // Navigate to the 'content' field
                String content = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
          
                
                // Total number of tokens used in the query
                int tokenTotal = rootNode
                        .path("usage")
                        .path("total_tokens")
                        .asInt();
                
                totalTokens += tokenTotal;

                // After getting the response from the API, add it to the history
                Map<String, String> assistantMessage = Map.of("role", "assistant", "content", content);
                this.messages.add(assistantMessage);

                System.out.println(responseBody);
                return content;

            } else {
                System.err.println("Request failed with code: " + response.code());
                System.err.println(response.body().string());
                return "Dave didn't want to talk because the request failed with code: " + response.code();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Dave didn't want to talk because there was an error processing the JSON: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Dave didn't want to talk because there was an IO exception: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Dave didn't want to talk because an exception occurred: " + e.getMessage();
        }

    }

    private static String mapToJson(Map<String, Object> map) {
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

