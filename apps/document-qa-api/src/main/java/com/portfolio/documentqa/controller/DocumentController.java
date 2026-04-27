package com.portfolio.documentqa.controller;

import com.portfolio.documentqa.service.DocumentService;
import com.portfolio.documentqa.service.QaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;
    private final QaService qaService;

    public DocumentController(DocumentService documentService, QaService qaService) {
        this.documentService = documentService;
        this.qaService = qaService;
    }

    @PostMapping("/documents")
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("file") @NotNull MultipartFile file) throws Exception {

        int chunks = documentService.ingest(file);
        return ResponseEntity.ok(Map.of(
                "filename", file.getOriginalFilename(),
                "chunks", chunks,
                "message", "Document ingested successfully"
        ));
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
            @Valid @RequestBody AskRequest request) {

        String answer = qaService.answer(request.question());
        return ResponseEntity.ok(Map.of(
                "question", request.question(),
                "answer", answer
        ));
    }

    record AskRequest(@NotBlank String question) {}
}
