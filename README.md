# java-explore-with-me

Фича - подписка на друзей

Подписка на друзей и возможность получать список актуальных событий, 
в которых они принимают участие.

Описание API находится в Docs/rest_api/subscription_api.yaml
(Файл необходимо загрузить в https://editor-next.swagger.io)

Весь код к фиче находится в main-service пакет ru.practicum.subscription

-------

Описание explore-with-me:

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


