# Java-explore-with-me
ссылка на Пулл-реквест https://github.com/alekseigrinko/java-explore-with-me/pull/2

## Это репозиторий проекта "Explore-with-me"
#### Реализован функционал, согласно представленному ТЗ

Приложение состоит из двух модулей:

Первый модуль main сервер:
1. Имеет публичный и административный режим, а также режим приватный режим пользователя
2. Регистрирует и хранит данные пользователей.
3. Хранит данные событий, которые размещают зарегистрированные пользователи. Пользователи и администратор могут их редактировать.
4. Пользователи могут оставлять запросы на участие событий. Инициаторы событий - подтверждают запросы и отклоняют запросы.
   Диаграмма базы данных модуля:
   ![explore@localhost](https://user-images.githubusercontent.com/98738143/199989589-d146f0ab-8a97-46cf-a29c-7344e7e5c4ef.png)

Запуск модуля осуществляется через файл:
ExploreServer пакета server
https://github.com/alekseigrinko/java-explore-with-me/blob/develop/server/src/main/java/ru/practicum/server/ExploreServer.java

Ссылка на сваггер файл:
https://github.com/alekseigrinko/java-explore-with-me/blob/develop/ewm-main-service-spec.json

Второй модуль сервер статистики:
1. Хранит данные статистики запросов пользователей через публичный контролер событий.
2. Предоставляет данные статистики
   Диаграмма базы данных модуля:
   ![explore-statistic](https://user-images.githubusercontent.com/98738143/199989695-21d19e90-414e-4f94-8fb5-db9be3ef4ba2.png)
   
   Запуск модуля осуществляется через файл:
   ExploreServer
   Запуск модуля осуществляется через файл:
   ExploreStatistic пакета statistic
   https://github.com/alekseigrinko/java-explore-with-me/blob/develop/statistic/src/main/java/ru/practicum/statistic/ExploreStatistic.java
   
Ссылка на сваггер файл:
https://github.com/alekseigrinko/java-explore-with-me/blob/develop/ewm-stats-service-spec.json

Для работы приложения необходимо запускать два файла одновременно.
Возможен запуск через docker-compose.yml в основном каталоге проекта
Приложение написано на Java. Пример кода:
```java
public class Main {
    public static void main(String[] args) {
    }
}
