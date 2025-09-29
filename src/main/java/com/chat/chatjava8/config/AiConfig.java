package com.chat.chatjava8.config;

import com.chat.chatjava8.core.ChatClient;
import com.chat.chatjava8.core.ChatModel;
import com.chat.chatjava8.core.OllamaModel;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Gawen
 * @Date 2025/9/29
 *
 * Bean装配
 */
@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

    @Bean
    public OkHttpClient okHttpClient(AiProperties p) {
        return new OkHttpClient().newBuilder()
                .connectTimeout(p.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(p.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(p.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public ChatModel chat(OkHttpClient httpClient, AiProperties p) {
        return new OllamaModel(p.getUrl(), p.getModel(), p.getApiKey(), httpClient);
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return new ChatClient(chatModel);
    }
}
