package com.example.application;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTHelper {
	
	public static String chatGPT(String message) {
		
		String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
		String API_KEY = "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";
		String API_VERSION = "gpt-3.5-turbo";
		

	        try {
	        	 OkHttpClient client = new OkHttpClient();
	             MediaType mediaType = MediaType.parse("application/json");

	             Map<String, Object> requestData = new HashMap<>();
	            // requestData.put("prompt", message);
	             requestData.put("max_tokens", 50); // Set the maximum number of tokens in the response as an integer
	             requestData.put("temperature", 0.7); // Set the temperature (higher values make the response more random)
	             requestData.put("n", 1); // Set the number of responses to generate
	             requestData.put("model", API_VERSION); // Specify the model version
	             requestData.put("messages", createMessages(message));



	             RequestBody requestBody = RequestBody.create(mediaType, mapToJson(requestData));
	             Request request = new Request.Builder()
	                     .url(API_ENDPOINT)
	                     .addHeader("Content-Type", "application/json")
	                     .addHeader("Authorization", "Bearer " + API_KEY)
	                     .post(requestBody)
	                     .build();

	             Response response = client.newCall(request).execute();
	             if (response.isSuccessful()) {
	                 String responseBody = response.body().string();
	                 System.out.println(responseBody);
	                 // Handle the response here
	                 return responseBody;
	                 
	             } else {
	                 System.err.println("Request failed with code: " + response.code());
	                 System.err.println(response.body().string());
	             }
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	        
	        return "Dave didn't want to talk :(";
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
	 
	 private static List<Map<String, String>> createMessages(String message) {
		    Map<String, String> systemMessage = Map.of("role", "system", "content", "You are a helpful assistant.");
		    Map<String, String> userMessage = Map.of("role", "user", "content", message);
		    return List.of(systemMessage, userMessage);
		}
	 
}
