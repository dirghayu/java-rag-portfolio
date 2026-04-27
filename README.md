# java-rag-portfolio

RAG and agentic AI apps built with Java 21, Spring Boot 3, LangChain4j, Weaviate, and Claude API.

[![CI](https://github.com/dirghayu/java-rag-portfolio/actions/workflows/ci.yml/badge.svg)](https://github.com/dirghayu/java-rag-portfolio/actions/workflows/ci.yml)

---

## Apps

| App | Description | Status |
|-----|-------------|--------|
| [Document Q&A API](apps/document-qa-api/) | Upload a PDF, ask questions about it. RAG over Weaviate with Claude as the LLM. | ✅ Complete |
| Job Description Analyser | Paste a JD, get a skills gap analysis against a stored resume. | ⬜ Planned |
| Multi-document Research Assistant | Ask cross-document questions with hybrid search (vector + BM25). | ⬜ Planned |
| Enterprise Knowledge Bot | Multi-tenant chat over a company knowledge base with JWT isolation. | ⬜ Planned |
| Agentic Task Planner | Goal-driven agent that breaks tasks down and queries a knowledge base. | ⬜ Planned |

---

## Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| RAG / LLM orchestration | LangChain4j |
| LLM | Anthropic Claude (claude-sonnet-4-6) |
| Vector DB | Weaviate Cloud (asia-southeast1) |
| PDF parsing | Apache Tika |
| Cloud | GCP / Cloud Run |
| CI/CD | GitHub Actions |

---

## App 1 — Document Q&A API

### Endpoints

```
POST /api/documents   multipart/form-data  file=<pdf>   — ingest document
POST /api/ask         application/json     {"question": "..."}  — ask a question
```

### How it works

1. PDF uploaded → Apache Tika extracts text
2. Text split into overlapping chunks (500 tokens, 50 overlap)
3. Each chunk embedded via Anthropic Embeddings API
4. Embeddings stored in Weaviate
5. At query time: question embedded → top-5 chunks retrieved → Claude answers with retrieved context

### Running locally

```bash
cd apps/document-qa-api
cp .env.example .env   # fill in your API keys
export $(cat .env | xargs)
mvn spring-boot:run
```

### Running tests

```bash
mvn test
```
