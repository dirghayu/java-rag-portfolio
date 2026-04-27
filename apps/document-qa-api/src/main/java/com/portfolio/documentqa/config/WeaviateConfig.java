package com.portfolio.documentqa.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeaviateConfig {

    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Value("${app.gemini.model}")
    private String geminiModel;

    @Value("${app.gemini.embedding-model}")
    private String embeddingModel;

    @Value("${app.weaviate.host}")
    private String weaviateHost;

    @Value("${app.weaviate.api-key}")
    private String weaviateApiKey;

    @Value("${app.weaviate.collection}")
    private String collection;

    @Bean
    public ChatLanguageModel chatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                .modelName(geminiModel)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return GoogleAiGeminiEmbeddingModel.builder()
                .apiKey(geminiApiKey)
                .modelName(embeddingModel)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return WeaviateEmbeddingStore.builder()
                .apiKey(weaviateApiKey)
                .scheme("https")
                .host(weaviateHost)
                .objectClass(collection)
                .build();
    }
}
