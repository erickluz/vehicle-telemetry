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
1. vehicle-telemetry-mock
Função: Gera dados simulados de veículos e envia para Kafka.

Gera dados para 10 veículos

Frequência: configurável (ex: 1 evento por segundo por veículo)

Publica no tópico Kafka: vehicle.telemetry.raw

Leve, stateless

Tecnologias: Java com Quarkus, SmallRye Kafka (producer)

2. telemetry-ingestion
Função: Recebe os dados do Kafka e armazena no MongoDB (sem transformação).

Consumidor Kafka do tópico vehicle.telemetry.raw

Insere os documentos como vieram no MongoDB (coleção raw_telemetry)

Opcional: pode validar esquema JSON

Tecnologias: Quarkus, Kafka (consumer), MongoDB, Panache

3. telemetry-processor
Função: Processa os dados recebidos (ex: calcula média, detecta anomalias) e envia para PostgreSQL ou outro tópico Kafka.

Consome vehicle.telemetry.raw

Aplica lógica de negócio:

Detecta velocidade > 120km/h

Motor > 100°C

Envia para:

PostgreSQL (processed_telemetry)

Kafka (vehicle.telemetry.alert)

Tecnologias: Kafka, PostgreSQL, Quarkus com JPA/Hibernate

4. alert-service
Função: Recebe eventos de alerta e gerencia status de alertas por veículo.

Kafka consumer do tópico vehicle.telemetry.alert

Armazena no PostgreSQL (alerts)

Pode expor endpoints REST para:

Listar alertas ativos

Resolver alertas

Histórico de alertas

Tecnologias: Quarkus, PostgreSQL

5. report-api
Função: API REST para o frontend ou sistemas terceiros consumirem relatórios.

Filtros por veículo, período, tipo de dado

Dados de:

MongoDB (raw_telemetry)

PostgreSQL (processed_telemetry, alerts)

Pode ter paginação e agregações simples

Tecnologias: Quarkus, RESTEasy, MongoDB, PostgreSQL

6. gateway 
Função: API Gateway para entrada única para o frontend.

Redireciona /api/reports, /api/alerts, etc.

Pode fazer autenticação

Controla CORS e versionamento

Tecnologias: Traefik, NGINX, ou um microserviço Quarkus REST

7. dashboard-ui 
Função: UI web em React para mostrar:

Mapa com posição dos veículos

Tabela de dados em tempo real

Lista de alertas

Filtros por veículo ou data

Tecnologias: React.js, Chart.js ou Recharts, REST, WebSocket 

8. telemetry-metrics
Função: Expõe métricas Prometheus como:

Eventos processados por minuto

Latência média

Número de veículos ativos

Alertas disparados

Tecnologias: Quarkus com Micrometer, Prometheus

9. auth-service 
Função: Gerenciar autenticação de usuários para acessar a API.

JWT (JSON Web Token)

Ou Keycloak com OAuth2

Integrável com o gateway

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
