# Microservices Learning

---

## Tools

### Installation Pack
1. pgAdmin (http://localhost:5050)

### Startup
```
docker-compose -f .\docker-compose.yml up -d
```

---

## Spring Microservices Starter

### Features
1. REST exception handler.
2. Flyway before and after migration listeners.

---

## Resource Service

### Installation Pack
1. PostgreSQL (http://localhost:5401)
2. Resource Service (http://localhost:8081)

### Startup
```
docker-compose -f .\resource-service\docker-compose.yml up -d
```

---

## Song Service

### Installation Pack
1. PostgreSQL (http://localhost:5402)
2. Song Service (http://localhost:8082)

### Startup
```
docker-compose -f .\song-service\docker-compose.yml up -d
```

---
