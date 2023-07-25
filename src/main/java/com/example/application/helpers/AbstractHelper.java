package com.example.application.helpers;

import com.example.application.helpers.utils.Helper;
import com.example.application.helpers.utils.HelperManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import oshi.util.tuples.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AbstractHelper implements IHelper {
    protected Helper helper;
    protected String customSystemMessage;

    protected AbstractHelper(Helper helper, String customSystemMessage) {
        this.helper = helper;
        this.customSystemMessage = customSystemMessage;
    }

    public String getName() {
        return helper.name;
    }

    public String getSystemMessage() {
        return helper.systemMessage == null ? customSystemMessage : helper.systemMessage;
    }

    public Pair<String, Integer> getResponse(String message, String key, String apiVersion,
                                             List<Map<String, String>> messageHistory, int tokens,
                                             double temperature) {
        String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
        int tokenTotal = 0;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");

            // Add the user's message to the history
            Map<String, String> userMessage = Map.of("role", "user", "content", message);
            messageHistory.add(userMessage);

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("max_tokens", tokens);
            requestData.put("temperature", temperature);
            requestData.put("n", 1);
            requestData.put("model", apiVersion);
            requestData.put("messages", messageHistory);

            RequestBody requestBody = RequestBody.create(mediaType, HelperManager.getInstance().mapToJson(requestData));
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
                tokenTotal = rootNode
                        .path("usage")
                        .path("total_tokens")
                        .asInt();

                // After getting the response from the API, add it to the history
                Map<String, String> assistantMessage = Map.of("role", "assistant", "content", content);
                messageHistory.add(assistantMessage);

                System.out.println(responseBody);
                return new Pair<>(content, tokenTotal);

            } else {
                System.err.println("Request failed with code: " + response.code());
                System.err.println(response.body().string());
                return new Pair<>(helper.name+" didn't want to talk because the request failed with code: " + response.code(), tokenTotal);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Pair<>(helper.name+" didn't want to talk because there was an error processing the JSON: " + e.getMessage(), tokenTotal);
        } catch (IOException e) {
            e.printStackTrace();
            return new Pair<>(helper.name+" didn't want to talk because there was an IO exception: " + e.getMessage(),
                   tokenTotal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(helper.name+" didn't want to talk because an exception occurred: " + e.getMessage(),
                    tokenTotal);
        }
    }
}
