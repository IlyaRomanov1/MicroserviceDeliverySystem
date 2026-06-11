# Notification Service

Сервис-подписчик на события заказов. Получает сообщения из Kafka и сохраняет их в базу данных.

## Технологии

- Java 17
- Spring Boot 3
- Spring Kafka
- Spring Data JPA
- PostgreSQL
- Lombok
- ModelMapper
- Docker

## Как работает

1. Подключается к Kafka broker
2. Слушает топик `order-send`
3. Получает `OrderViewDto` (userId + totalPrice)
4. Сохраняет в таблицу `orders` своей БД
