# 🚗 Vehicle Telemetry Monitoring System

Este projeto é um sistema de **monitoramento de veículos via telemetria**, desenvolvido com uma arquitetura de **microserviços**. Foi criado com foco em estudo e prática de tecnologias modernas, incluindo:

- Kubernetes (Minikube)
- MongoDB
- PostgreSQL
- Apache Kafka
- Kafdrop (painel Kafka)
- Java + Quarkus (serviços)

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

## 📦 Estrutura de Microsserviços (exemplo)

| Serviço                  | Função                                                           | Banco     | Kafka? |
|--------------------------|------------------------------------------------------------------|-----------|--------|
| `vehicle-service`        | Cadastro e atualização de veículos                               | PostgreSQL| envia  |
| `telemetry-ingestor`     | Recebimento de dados de sensores                                 | MongoDB   | envia  |
| `telemetry-processor`    | Processamento de eventos de telemetria em tempo real             | MongoDB   | lê     |
| `alert-service`          | Geração de alertas em caso de anomalias                          | PostgreSQL| lê     |
| `user-service`           | Gestão de usuários, permissões, autenticação                     | PostgreSQL| -      |

---

## 🚀 Subindo o ambiente

### Pré-requisitos

- Docker
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- Kubectl
- (Opcional) Helm

### Subindo a infraestrutura:

```bash
kubectl create namespace dev-infra
kubectl apply -f infra-all.yaml
