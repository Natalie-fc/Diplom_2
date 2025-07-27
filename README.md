# Diplom_2
# Autotests for Stellar Burgers API

Автоматические тесты для проверки API сервиса [Stellar Burgers](https://stellarburgers.nomoreparties.site).

## Технологии

| Технология     | Версия     |
|----------------|------------|
| Java           | 11         |
| Maven          | 3.9.0      |
| JUnit          | 4.13.2     |
| REST Assured   | 5.3.0      |
| Allure         | 2.23.0     |

## 📁 Структура проекта

- enums — перечисления:
    - Ingredient — валидные/невалидные идентификаторы ингредиентов
- model — модели данных для сериализации/десериализации (User, LoginRequest, Order)
- steps — шаги API (UserSteps, OrderSteps)
- tests — тесты:
    - UserTests — регистрация пользователя
    - LoginTests — авторизация
    - OrderTests — создание заказа
- BaseTest — базовая настройка RestAssured
- util/RestConfig — базовый URL API

## ⚙ Как запустить тесты

1. Склонируй репозиторий:
   ```bash
   git clone https://github.com/Natalie-fc/Diplom_2.git
   cd Diplom_2