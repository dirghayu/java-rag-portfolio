package com.portfolio.documentqa.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QaService {

    private static final PromptTemplate PROMPT_TEMPLATE = PromptTemplate.from("""
            You are a helpful assistant answering questions about documents.
            Use only the context below to answer. If the answer is not in the context, say so clearly.

            Context:
            {{context}}

            Question: {{question}}

            Answer:
            """);

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final AnthropicChatModel chatModel;
    private final int maxResults;

    public QaService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            AnthropicChatModel chatModel,
            @Value("${app.rag.max-results}") int maxResults) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.chatModel = chatModel;
        this.maxResults = maxResults;
    }

    public String answer(String question) {
        Embedding questionEmbedding = embeddingModel.embed(question).content();

        List<EmbeddingMatch<TextSegment>> matches =
                embeddingStore.findRelevant(questionEmbedding, maxResults);

        String context = matches.stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n\n---\n\n"));

        Prompt prompt = PROMPT_TEMPLATE.apply(Map.of(
                "context", context,
                "question", question
        ));

        return chatModel.generate(prompt.toUserMessage()).content().text();
    }
}
