# Weaviate

## Why Weaviate

Most RAG stacks need a vector database. Weaviate was chosen over alternatives (Pinecone, ChromaDB, pgvector) for these reasons:

- **Java-native client** — LangChain4j has a first-class Weaviate integration. No Python SDK workarounds.
- **Hybrid search out of the box** — combines vector similarity (ANN) with BM25 keyword search in a single query. Critical for App 3 (multi-document research).
- **Multi-tenancy API** — built-in tenant isolation per collection. Required for App 4 (enterprise knowledge bot).
- **Managed cloud on GCP** — Weaviate Cloud runs on GCP asia-southeast1, same region as the rest of this stack. Lower latency, simpler networking.
- **Production-grade** — used in production by companies like Spotify, Stack Overflow, and Ikea. Not a toy.

---

## Pros

| Pro | Detail |
|-----|--------|
| Hybrid search | BM25 + vector in one query, no extra tooling |
| Multi-tenancy | Per-tenant data isolation built into the schema |
| Schema flexibility | Can store structured metadata alongside vectors |
| GraphQL + REST + gRPC | Multiple query interfaces |
| LangChain4j integration | `WeaviateEmbeddingStore` works out of the box |
| Managed cloud | No infra to manage for portfolio use |
| Free sandbox | 14-day renewable sandbox clusters on Weaviate Cloud |

## Cons

| Con | Detail |
|-----|--------|
| 14-day sandbox expiry | Free tier clusters expire — need to recreate or upgrade for long-running demos |
| Learning curve | GraphQL query syntax is unfamiliar compared to SQL |
| Overkill for small data | For < 10k documents, pgvector on Postgres is simpler |
| Cold start latency | Sandbox clusters can be slow on first request after idle |

---

## Key Concepts

**Collection** — equivalent to a table. Stores objects with vector embeddings + metadata properties.

**Object** — a single record. Has a UUID, a vector, and a set of properties (text, numbers, dates).

**Tenant** — an isolated partition within a collection. Used for multi-tenant apps where different users must not see each other's data.

**Hybrid search** — `alpha` parameter controls the blend: `alpha=0` is pure BM25 keyword, `alpha=1` is pure vector, `alpha=0.5` is equal weight.

---

## Startup Steps

### 1. Create a Weaviate Cloud account
Sign up at https://console.weaviate.cloud — free, no credit card.

### 2. Create a sandbox cluster
- Click **Create cluster**
- Choose **Free sandbox**
- Select region: `GCP asia-southeast1` (or closest to you)
- Copy the **REST endpoint** and **API key**

### 3. Add credentials to `.env`
```
WEAVIATE_HOST=your-cluster.weaviate.cloud
WEAVIATE_API_KEY=your-api-key
```

### 4. Collections are auto-created
`WeaviateEmbeddingStore` in LangChain4j creates the collection automatically on first write. No manual schema setup needed for basic use.

### 5. Inspect your data
Use the Weaviate Cloud console → **Query** tab to run GraphQL queries against your cluster and verify documents were ingested correctly:

```graphql
{
  Get {
    Documents {
      text
      _additional { id distance }
    }
  }
}
```

---

## Useful Links

- [Weaviate Cloud Console](https://console.weaviate.cloud)
- [Weaviate Docs — Quickstart](https://weaviate.io/developers/weaviate/quickstart)
- [Weaviate Docs — Hybrid Search](https://weaviate.io/developers/weaviate/search/hybrid)
- [Weaviate Docs — Multi-tenancy](https://weaviate.io/developers/weaviate/manage-data/multi-tenancy)
- [LangChain4j Weaviate Integration](https://docs.langchain4j.dev/integrations/embedding-stores/weaviate)
- [Weaviate Java Client](https://weaviate.io/developers/weaviate/client-libraries/java)
