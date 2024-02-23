# Microservices Learning

## Stack

### Services

#### Eureka Server
- Application (http://localhost:8761)

#### Gateway
- Application (http://localhost:8084)

#### Resource Processor
- Application (http://localhost:8083)

#### Resource Service
- Application (http://localhost:8081)
- PostgreSQL (http://localhost:5401)

#### Song Service
- Application (http://localhost:8082)
- PostgreSQL (http://localhost:5402)

#### Storage Service
- Application (http://localhost:8085)
- PostgreSQL (http://localhost:5405)

### Dependencies

#### Resource Service API
- Resource Service inbound adapter.

#### Song Service API
- Song Service inbound adapter.

#### Spring Microservices Starter
- AWS S3 client.
- Flyway before and after migration listeners.
- REST exception handler.

#### Storage Service API
- Storage Service inbound adapter.

### Tools

#### LocalStack
- Application (http://localhost:4566)

#### RabbitMQ
- Application (http://localhost:5672)

## Binaries

### Artifacts
- [Eureka Server](https://github.com/MaksimKavalenka/microservices-learning/packages/2064895)
- [Gateway](https://github.com/MaksimKavalenka/microservices-learning/packages/2064892)
- [Resource Processor](https://github.com/MaksimKavalenka/microservices-learning/packages/2046204)
- [Resource Service](https://github.com/MaksimKavalenka/microservices-learning/packages/2043819)
- [Resource Service API](https://github.com/MaksimKavalenka/microservices-learning/packages/2046203)
- [Song Service](https://github.com/MaksimKavalenka/microservices-learning/packages/2043571)
- [Song Service API](https://github.com/MaksimKavalenka/microservices-learning/packages/2043572)
- [Spring Microservices Starter](https://github.com/MaksimKavalenka/microservices-learning/packages/2043488)
- [Storage Service](https://github.com/MaksimKavalenka/microservices-learning/packages/2073648)
- [Storage Service API](https://github.com/MaksimKavalenka/microservices-learning/packages/2073649)

### Docker Images
- [Eureka Server](https://hub.docker.com/r/maksimkavalenka/microservices-learning.eureka-server)
- [Gateway](https://hub.docker.com/r/maksimkavalenka/microservices-learning.gateway)
- [Resource Processor](https://hub.docker.com/r/maksimkavalenka/microservices-learning.resource-processor)
- [Resource Service](https://hub.docker.com/r/maksimkavalenka/microservices-learning.resource-service)
- [Song Service](https://hub.docker.com/r/maksimkavalenka/microservices-learning.song-service)
- [Storage Service](https://hub.docker.com/r/maksimkavalenka/microservices-learning.storage-service)

### Helm Charts

- [LocalStack](https://localstack.github.io/helm-charts)
- [Microservices Learning](https://maksimkavalenka.github.io/microservices-learning)
- [RabbitMQ](https://github.com/bitnami/charts/tree/main/bitnami/rabbitmq)
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add localstack https://localstack.github.io/helm-charts
helm repo add microservices-learning https://maksimkavalenka.github.io/microservices-learning
helm repo update
```

## Startup

### Helm

```
helm install localstack localstack/localstack -f ./configs/localstack/values.yaml
helm install rabbitmq bitnami/rabbitmq -f ./configs/rabbitmq/values.yaml
helm install storage-service microservices-learning/storage-service
helm install song-service microservices-learning/song-service
helm install resource-service microservices-learning/resource-service
helm install resource-processor microservices-learning/resource-processor
helm install gateway microservices-learning/gateway
```

### Docker
```
docker-compose -f .\docker-compose.yml up -d
```
