# Microservices Learning

## Common (optional)

### Prerequisites
1. pgAdmin

### Startup
1. docker-compose -f .\docker-compose.yml up -d

---

## Resource Service

### Prerequisites
1. PostgreSQL

### Startup
1. docker-compose -f .\resource-service\docker-compose.yml up -d

### Manual Configuration
1. Log into the PostgreSQL instance at http://localhost:5401 (admin:admin).
2. Create a database with the name **resource**.

---
