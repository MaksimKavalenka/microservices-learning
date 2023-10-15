# Microservices Learning

## Startup
```
docker-compose -f .\docker-compose.yml up -d
```

## Services

### Eureka Server

#### Installation Pack
1. Eureka Server (http://localhost:8761)

### Resource Service

#### Installation Pack
1. Resource Service (http://localhost:8081)
2. PostgreSQL (http://localhost:5401)

### Song Service

#### Installation Pack
1. Song Service (http://localhost:8082)
2. PostgreSQL (http://localhost:5402)

## Libraries

### Spring Microservices Starter

#### Features
1. REST exception handler.
2. Flyway before and after migration listeners.

## Tools

### Installation Pack
1. pgAdmin (http://localhost:5050)

### Startup
```
docker-compose -f .\docker-compose-tools.yml up -d
```
