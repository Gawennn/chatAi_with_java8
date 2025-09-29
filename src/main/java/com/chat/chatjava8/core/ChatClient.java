package com.chat.chatjava8.core;

/**
 * @author Gawen
 * @Date 2025/9/29
 */
public class ChatClient {
    private final ChatModel model;

    public ChatClient(ChatModel model) {
        this.model = model;
    }

    public String chat(String message) {
        return model.chat(message);
    }
}
