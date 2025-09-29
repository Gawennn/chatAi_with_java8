package com.chat.chatjava8.controller;

import com.chat.chatjava8.core.ChatClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author Gawen
 * @Date 2025/9/29
 */
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return chatClient.chat(message);
    }
}
