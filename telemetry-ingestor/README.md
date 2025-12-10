# telemetry-ingestor

Projeto base do microserviço **Telemetry Ingestor**, responsável por receber e armazenar eventos de telemetria. Foi gerado com Quarkus 3.27.1 e configurado para Java 21.

## Dependências incluídas
- Reactive Messaging com Kafka
- MongoDB Reactive
- RESTEasy Reactive (com Jackson)
- SmallRye OpenAPI
- Lombok (scope provided)
- Testes com JUnit 5 e RestAssured

## Executando em modo de desenvolvimento
Use o dev mode do Quarkus:

```bash
./mvnw quarkus:dev
```

A Dev UI fica disponível em `http://localhost:8082/q/dev/`.

## Empacotando a aplicação
Para gerar o artefato JVM:

```bash
./mvnw package
```

O resultado ficará em `target/quarkus-app/`.

## Configurações principais (application.properties)
- Porta HTTP: `8082`
- MongoDB: `mongodb://localhost:27017` (database `telemetrydb`)
- Kafka consumer: tópico `vehicle-telemetry` em `localhost:9092`
