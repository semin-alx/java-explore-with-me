# java-explore-with-me

pull request
--------------
https://github.com/semin-alx/java-explore-with-me/pull/1

Описание:

Приложение состоит из трех модулей:
main-service - главный сервис
statatistic-service - сервис статистики
expl-me-common - модуль с общим функционалом

Запуск осуществляется в 4 докерах
1. главный сервис (main-service слушает порт 8080)
2. сервис статистики (statatistic-service порт 9090)
3. База данных главного сервиса
4. База данных сервиса статистики

Запуск осуществляется командами:
mvn clean package
docker-compose up


