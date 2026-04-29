# java-rag-portfolio

## What This Is
5 RAG/agentic AI apps built in Java to demonstrate senior-level AI engineering for a job application. Built to show: Java 21, Spring Boot 3, LangChain4j, Weaviate, Gemini API, Tavily, GCP deployment, GitHub Actions CI/CD.

## Apps

| # | App | Status |
|---|-----|--------|
| 1 | Document Q&A API | ✅ Scaffolded — needs local run + test |
| 2 | Job Description Analyser | ⬜ Not started |
| 3 | Multi-document Research Assistant | ⬜ Not started |
| 4 | Enterprise Knowledge Bot | ⬜ Not started |
| 5 | Agentic Task Planner | ⬜ Not started |

## Stack (consistent across all apps)

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.2.5 |
| RAG orchestration | LangChain4j 0.31.0 |
| LLM | Google Gemini (`gemini-1.5-flash`) |
| Embeddings | Gemini `text-embedding-004` |
| Vector DB | Weaviate Cloud (GCP asia-southeast1) |
| PDF parsing | Apache Tika 2.9.2 |
| Web search (agentic) | Tavily API |
| Cloud | GCP — Cloud Run |
| CI/CD | GitHub Actions |

## API Keys
All keys are in `apps/document-qa-api/.env` (gitignored). Never commit this file.

```
GEMINI_API_KEY=AIzaSyBrazs_yLPPC-bJemTqXMO98GDpcQnc_1Y
TAVILY_API_KEY=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
WEAVIATE_HOST=tmeu4g7rsmukw36p1qr1hw.c0.asia-southeast1.gcp.weaviate.cloud
WEAVIATE_API_KEY=SHhXaDdXMENhc1FHbm9PdF9IOXJ2N3lJN0ZweGFJU2x3SmRtSTJieWZSQ3BVV09LMXE2YysxOXFKNHpJPV92MjAw
```

## Weaviate Cloud
- Console: https://console.weaviate.cloud
- Cluster: `tmeu4g7rsmukw36p1qr1hw.c0.asia-southeast1.gcp.weaviate.cloud`
- gRPC: `grpc-tmeu4g7rsmukw36p1qr1hw.c0.asia-southeast1.gcp.weaviate.cloud`
- Free sandbox — 14-day expiry, renewable
- Collection name: `Documents`

## App 1 — Document Q&A API

### What's built
- `POST /api/documents` — upload PDF, Tika extracts text, chunks into 500-token overlapping segments, embeds via Gemini, stores in Weaviate
- `POST /api/ask` — embeds question, retrieves top-5 chunks from Weaviate, answers with Gemini via prompt template
- MockMvc tests for controller layer (mocks DocumentService and QaService)
- GitHub Actions CI pipeline

### Key files
```
apps/document-qa-api/
├── pom.xml
├── .env                          # gitignored — real keys here
├── .env.example                  # committed — template
└── src/main/java/com/portfolio/documentqa/
    ├── DocumentQaApplication.java
    ├── config/WeaviateConfig.java     # Gemini + Weaviate beans
    ├── controller/DocumentController.java
    ├── service/DocumentService.java   # Tika + chunking + embedding + store
    └── service/QaService.java         # retrieval + Gemini answer
```

### Running locally (Windows PowerShell)
```powershell
cd C:\DK\study\java-rag-portfolio\apps\document-qa-api
$env:GEMINI_API_KEY="AIzaSyBrazs_yLPPC-bJemTqXMO98GDpcQnc_1Y"
$env:WEAVIATE_HOST="tmeu4g7rsmukw36p1qr1hw.c0.asia-southeast1.gcp.weaviate.cloud"
$env:WEAVIATE_API_KEY="SHhXaDdXMENhc1FHbm9PdF9IOXJ2N3lJN0ZweGFJU2x3SmRtSTJieWZSQ3BVV09LMXE2YysxOXFKNHpJPV92MjAw"
mvn spring-boot:run
```

### Test endpoints
```powershell
# Upload a PDF
curl -F "file=@yourfile.pdf" http://localhost:8080/api/documents

# Ask a question
curl -X POST http://localhost:8080/api/ask `
  -H "Content-Type: application/json" `
  -d '{"question":"What is this document about?"}'
```

### Run tests
```powershell
mvn test
```

## Next Steps (priority order)
1. Run App 1 locally and verify end-to-end with a real PDF
2. Build App 2 — Job Description Analyser (RAG over resume, React frontend)
3. Build App 3 — Multi-document Research Assistant (hybrid search)
4. Build App 4 — Enterprise Knowledge Bot (multi-tenancy + JWT)
5. Build App 5 — Agentic Task Planner (LangChain4j Tools API + Tavily)

## GitHub
- Repo: https://github.com/dirghayu/java-rag-portfolio
- Branch: main
- CI: GitHub Actions (`.github/workflows/ci.yml`)

## Notes
- Firebase CLI always use `npx` prefix on Windows
- LangChain4j version pinned at 0.31.0 — check for updates before each new app
- QaService depends on `ChatLanguageModel` interface (not Gemini directly) — easy to swap LLM
- Tavily is for web search in agentic apps — not yet wired up
