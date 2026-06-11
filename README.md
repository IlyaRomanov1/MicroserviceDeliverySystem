# Microservice Delivery System

Микросервисная система для обработки заказов с использованием Kafka и Docker.

## Технологии

- **Java 17**, Spring Boot, Spring Kafka
- **Apache Kafka** (кластер из 3 нод в режиме KRaft)
- **PostgreSQL** (отдельные БД для каждого сервиса)
- **Docker Compose** (полная контейнеризация)
- **Spring Security** (JWT-аутентификация)

## Архитектура

- **order-service** — создаёт заказы, отправляет события в Kafka
- **notification-service** — получает события, сохраняет уведомления в БД
- **Kafka cluster** — 3 брокера, репликация сообщений
- **PostgreSQL** — `new_order_service_db` и `notification_db`

## Запуск

### Требования
- Docker Desktop
- Maven
- Java 17

### Шаги

```bash
# 1. Собрать jar-файлы
cd order-service && mvn clean package && cd ..
cd notification-service && mvn clean package && cd ..

# 2. Запустить все сервисы
docker-compose up -d

# 3. Проверить статус
docker-compose ps
