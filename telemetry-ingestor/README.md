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
- Kafka producer: tópico `vehicle-telemetry` em `localhost:9092` (canal `vehicle-telemetry-out`)
- Kafka consumer: tópico `vehicle-telemetry` em `localhost:9092` (canal `vehicle-telemetry-in`)

## Endpoints
- `POST /telemetries`: recebe um payload de telemetria, valida, salva no MongoDB e publica no Kafka. Exemplo:

```json
{
  "vehicleId": "123",
  "timestamp": "2024-05-20T10:15:30Z",
  "location": {"lat": -23.5, "lon": -46.6},
  "speed": 78.5,
  "fuelLevel": 52.3,
  "rpm": 3200
}
```

## Consumo via Kafka
Mensagens recebidas no tópico `vehicle-telemetry` também são persistidas diretamente no MongoDB pelo consumidor reativo configurado.
