package com.chat.chatjava8.core;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Gawen
 * @Date 2025/9/29
 *
 * ollama 模型
 */
public class OllamaModel implements ChatModel {

    Logger log = LoggerFactory.getLogger(OllamaModel.class);

    private final String baseUrl;
    private final String model;
    private final String apiKey;
    private final OkHttpClient http;

    private final ObjectMapper mapper = new ObjectMapper();

    public OllamaModel(String baseUrl, String model, String apiKey, OkHttpClient http) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.apiKey = apiKey;
        this.http = http;
    }

    // 非流式输出
    @Override
    public String chat(String userMessage) {
        try {
            log.info("userMessage={}", userMessage);

            String url = baseUrl.endsWith("/")
                    ? baseUrl + "api/chat"
                    : baseUrl + "/api/chat";

            // requestBody
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", model);
            // messages
            Map<String, String> user = new LinkedHashMap<>();
            user.put("role", "user");
            user.put("content", userMessage);
            body.put("messages", Arrays.asList(user));
            body.put("stream", false);  // 非流式输出

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    mapper.writeValueAsString(body)
            );

            Request.Builder builder = new Request.Builder().url(url).post(requestBody);

            try (Response resp = http.newCall(builder.build()).execute()) {
                if (!resp.isSuccessful()) {
                    return "Ollama API error: HTTP " + resp.code();
                }
                String json = resp.body() != null ? resp.body().string() : "";
                log.info("Ollama response: {}", json);
                JsonNode root = mapper.readTree(json);
                JsonNode messageNode = root.path("message");
                if (!messageNode.isMissingNode() && messageNode.has("content")) {
                    return messageNode.get("content").asText();
                } else {
                    return "No content from Ollama Model.";
                }

            }

        } catch (Exception e) {
            return "Ollama API request failed: " + e.getMessage();
        }
    }
}
