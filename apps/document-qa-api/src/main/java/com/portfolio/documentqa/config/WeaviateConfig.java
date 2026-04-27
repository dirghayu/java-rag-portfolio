package com.portfolio.documentqa.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.anthropic.AnthropicEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeaviateConfig {

    @Value("${app.anthropic.api-key}")
    private String anthropicApiKey;

    @Value("${app.anthropic.model}")
    private String anthropicModel;

    @Value("${app.weaviate.host}")
    private String weaviateHost;

    @Value("${app.weaviate.api-key}")
    private String weaviateApiKey;

    @Value("${app.weaviate.collection}")
    private String collection;

    @Bean
    public AnthropicChatModel chatModel() {
        return AnthropicChatModel.builder()
                .apiKey(anthropicApiKey)
                .modelName(anthropicModel)
                .maxTokens(1024)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return AnthropicEmbeddingModel.builder()
                .apiKey(anthropicApiKey)
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
