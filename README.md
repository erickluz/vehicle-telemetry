# 🚗 Vehicle Telemetry Monitoring System

Este projeto é um sistema de **monitoramento de veículos via telemetria**, desenvolvido com uma arquitetura de **microserviços**. Foi criado com foco em estudo e prática de tecnologias modernas, incluindo:

- Kubernetes (Minikube)
- MongoDB
- PostgreSQL
- Apache Kafka
- Kafdrop (painel Kafka)
- Java + Quarkus (serviços)

🧩 Visão Geral do Sistema
Um sistema de backend que recebe dados de telemetria de veículos (ex: localização, velocidade, consumo, ignição, temperatura), os armazena e gera informações derivadas (ex: consumo médio, tempo ligado, distância percorrida), com dashboards para consulta e alertas em tempo real.
---

## 🧠 Objetivo

Criar um sistema realista e completo que permita praticar:

- Arquitetura baseada em microsserviços
- Comunicação assíncrona com **Kafka**
- Persistência poliglota (**MongoDB** para dados não estruturados e **PostgreSQL** para dados relacionais)
- Deploy local com **Minikube**
- Observabilidade e gestão de infraestrutura

---

## 🛠️ Tecnologias e Ferramentas

| Tecnologia     | Descrição                                         |
|----------------|---------------------------------------------------|
| **Kubernetes** | Orquestração de containers com Minikube          |
| **MongoDB**    | Armazenamento de dados de telemetria             |
| **PostgreSQL** | Armazenamento de dados administrativos e usuários|
| **Apache Kafka** | Broker de mensagens para comunicação assíncrona |
| **Kafdrop**    | Interface web para inspecionar tópicos Kafka     |
| **Quarkus**    | Framework Java cloud-native para os microserviços|

---

## 📦 Estrutura de Microsserviços

| Serviço                  | Função                                                           | Banco     | Kafka? |
|--------------------------|------------------------------------------------------------------|-----------|--------|
| `vehicle-service`        | Cadastro e atualização de veículos                               | PostgreSQL| envia  |
| `telemetry-ingestor`     | Recebimento de dados de sensores                                 | MongoDB   | envia  |
| `telemetry-processor`    | Processamento de eventos de telemetria em tempo real             | MongoDB   | lê     |
| `alert-service`          | Geração de alertas em caso de anomalias                          | PostgreSQL| lê     |
| `user-service`           | Gestão de usuários, permissões, autenticação                     | PostgreSQL| -      |

| Serviço                 | Função                                                                     | Armazenamento    |
| ----------------------- | -------------------------------------------------------------------------- | ---------------- |
| **Telemetry Collector** | Recebe e valida dados brutos de telemetria                                 | MongoDB          |
| **Telemetry Processor** | Processa dados brutos, calcula métricas, detecta anomalias                 | MongoDB / Fila   |
| **Vehicle Registry**    | CRUD de veículos, usuários, permissões                                     | Banco Relacional |
| **Reporting API**       | Exposição de relatórios e dashboards (distância, consumo, histórico)       | Banco Relacional |
| **Alert Service**       | Gera e publica alertas com base em regras                                  | Fila + MongoDB   |
| **Gateway API**         | Entrada principal de APIs externas (usando Quarkus REST ou Spring Gateway) | -                |

---

🔍 Detalhes por serviço
1. Vehicle-Service
Responsável pelo cadastro e gestão dos veículos monitorados.

Tecnologias:

Quarkus (REST, Hibernate ORM, PostgreSQL)

PostgreSQL (armazenamento principal)

Requisitos:

Criar, listar, atualizar e remover veículos.

Cada veículo possui: id, placa, marca, modelo, ano, status (ativo/inativo), dataCadastro.

Endpoint REST para buscar veículo por placa ou ID.

Publicar evento no Kafka ao criar/atualizar um veículo (vehicle-created, vehicle-updated).

2. Telemetry-Ingest-Service
Responsável por receber os dados de telemetria (mockados ou de dispositivos reais).

Tecnologias:

Quarkus (Kafka Reactive Client)

MongoDB (armazenamento de eventos de telemetria)

Kafka (entrada de dados)

Requisitos:

Consumir dados do tópico Kafka vehicle-telemetry.

Persistir os dados brutos de telemetria no MongoDB com timestamp.

Validar formato e ignorar mensagens inválidas.

Estrutura de telemetria:

{
  "vehicleId": "uuid",
  "timestamp": "ISO8601",
  "location": {"lat": -23.5, "lon": -46.6},
  "speed": 78.5,
  "fuelLevel": 52.3,
  "rpm": 3200
}

3. Telemetry-Analytics-Service
Responsável por processar dados e gerar informações úteis (distância, consumo, etc).

Tecnologias:

Quarkus (Scheduler, Kafka, MongoDB, REST)

Kafka (entrada/saída de dados processados)

Requisitos:

Rodar periodicamente (ex: a cada 5 minutos) para processar dados do MongoDB.

Calcular métricas por veículo, como:

Distância percorrida.

Velocidade média.

Consumo médio estimado.

Publicar resultado em tópico Kafka vehicle-analytics.

Expor endpoint REST para consultar métricas agregadas de um veículo (via Mongo ou cache).

4. Report-Service
Responsável por gerar relatórios sob demanda ou por agendamento.

Tecnologias:

Quarkus (REST, Kafka, PostgreSQL)

Kafka (consumidor de vehicle-analytics)

Requisitos:

Consumir métricas do Kafka e armazenar no PostgreSQL (métricas históricas).

Expor REST para:

Buscar relatórios por veículo e intervalo de tempo.

Exportar relatório em JSON.

Pode ser estendido para envio por e-mail em background (extra).

---
📥 Fluxo de Dados
Dispositivo envia dados → Telemetry Collector (ex: HTTP ou Kafka)

Telemetry Collector envia para MongoDB e publica mensagem na fila

Telemetry Processor consome da fila, gera informações derivadas, grava no MongoDB e/ou PostgreSQL

Alert Service também consome eventos da fila e dispara alertas

Reporting API consulta banco relacional (dados processados) e/ou MongoDB (dados brutos) para montar dashboards

📦 Tecnologias para cada item
Recurso	Tecnologia Sugerida
Fila de Mensagens	Kafka (mais realista) ou RabbitMQ
Banco Relacional	PostgreSQL ou MySQL
NoSQL	MongoDB
Microsserviços	Java (Quarkus recomendado)
Deploy	Kubernetes (via Minikube)
Observabilidade	Prometheus + Grafana ou Elastic APM
Logs	Elasticsearch + Kibana ou Loki

📊 Requisitos Funcionais
 Registrar veículos e seus dados técnicos

 Registrar usuários e permissões de acesso

 Receber dados brutos via API (JSON com timestamp, velocidade, ignição, localização, etc)

 Armazenar dados brutos de telemetria

 Calcular métricas como: consumo médio, tempo ligado, distância, paradas

 Gerar alertas como: “veículo parado com motor ligado por mais de X minutos”

 Expor relatórios por API (filtragem por período, veículo, métricas)

 Expor métricas Prometheus (por serviço)

 Criar dashboards com Grafana




## 🚀 Subindo o ambiente

### Pré-requisitos

- Docker
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- Kubectl
-  Helm

### Subindo a infraestrutura:

```bash
kubectl create namespace dev-infra
kubectl apply -f infra-all.yaml
