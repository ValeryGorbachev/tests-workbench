Шаблон для простых проектов с ипользованием spring-boot и Angular.
====================================================================

## Тестовый csv файл 

Для загрузки тестовых ЦМ, он подходит для двух форм. в корне лежит готовый csv файл goods_receipt_test.csv.
Структура файла: Название товара(оно уже должно быть в бд и должно быть уникальным); количество товара; цена товара

## Запуск

Для запуска нужен установленный в системе Docker с плагином compose, либо для Windows/MacOS - Docker Desktop.

```sh
cd docker
docker compose build
docker compose up
```
Открыть брауер по ссылке [http://localhost](http://localhost])

## Разработка
Для сборки проекта вне контейнера понадобятся Java 21 и NodeJS 22.19.0.
### API
Построен на базе spring-boot 3.5, используются spring-data-jpa, spring-webmvc, mapstruct, lombok
### UI
Пострен на основе шаблона [sakai-ng 20.0.0](https://github.com/primefaces/sakai-ng)
