# Rating My Spot (RMS)

Платформа для поиска удобных мест для работы (кафе, коворкинги, библиотеки) с учётом практических критериев: скорость Wi‑Fi, наличие розеток и уровень шума. Backend — Spring Boot 3 (Java 17), JWT, PostgreSQL. Frontend — React 18 + TypeScript.

## Содержание
- Архитектура и стек
- Быстрый старт (Backend + DB)
- Frontend (Vite + React)
- Конфигурация окружения
- Основные эндпоинты API (v1)
- Геопоиск и PostGIS
- Тестирование
- Docker и CI/CD

## Архитектура и стек
- Backend: Java 17, Spring Boot 3, Spring Data JPA, Spring Security (JWT)
- DB: PostgreSQL (в проде рекомендуется PostGIS для геопоиска)
- Тесты: JUnit 5, Mockito, Testcontainers
- Контейнеризация: Docker, docker-compose
- CI/CD: GitLab CI
- Frontend: React 18 + TypeScript, Vite, Tailwind, React Router, Zustand, Axios, React-Leaflet

Слойность: REST контроллеры → сервисы → репозитории → БД. Stateless, JWT в заголовке Authorization: Bearer <token>.

## Быстрый старт (Backend + DB)
1) Зависимости: Java 17, Docker (Compose). Для сборки локально — Maven.

2) Запуск БД и приложения в контейнерах:
```bash
docker compose up -d
```
По умолчанию поднимется PostgreSQL и (после сборки JAR) приложение. Для разработки обычно запускают БД в контейнере, а backend — из IDE:
```bash
mvn spring-boot:run
```

3) Конфигурация по умолчанию (`src/main/resources/application.yml`):
- SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/rms
- SPRING_DATASOURCE_USERNAME: postgres
- SPRING_DATASOURCE_PASSWORD: postgres
- JWT_SECRET: please-change-me-in-prod-32-bytes-minimum-please

## Frontend (Vite + React)
Код в каталоге `frontend/`.
```bash
cd frontend
npm install
echo "VITE_API_URL=http://localhost:8080/api/v1" > .env.local
echo "VITE_MAP_TILE_URL=https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" >> .env.local
npm run dev
```
Доступно по адресу: http://localhost:5173 (по умолчанию Vite).

## Конфигурация окружения
Backend (env переменные):
- SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
- JWT_SECRET, JWT_EXPIRATION_SECONDS
- PORT (опционально)

Frontend (`.env.local`):
- VITE_API_URL, VITE_MAP_TILE_URL

## Основные эндпоинты API (v1)
Базовый префикс: `/api/v1`.

Аутентификация:
- POST `/auth/signup` — регистрация, body: { email, password } → { token }
- POST `/auth/signin` — логин, body: { email, password } → { token }

Споты (заведения):
- GET `/spots` — список с фильтрами и пагинацией: `type, minWifi, minOutlets, minNoise, maxNoise, page, size`
- GET `/spots/{id}` — детальная карточка
- GET `/spots/search?lat=..&lon=..&radius=..` — поиск по радиусу (Haversine)
- POST `/spots` — создание (ROLE_ADMIN)

Отзывы:
- GET `/spots/{spotId}/reviews` — список отзывов по месту
- POST `/spots/{spotId}/reviews` — создать отзыв (требуется JWT)
- PUT `/reviews/{id}` — изменить свой отзыв (или ADMIN)
- DELETE `/reviews/{id}` — удалить свой отзыв (или ADMIN)

Пример авторизации и запроса:
```bash
# регистрация
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"secret"}'

# логин
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"secret"}' | jq -r .token)

# чтение спотов
curl http://localhost:8080/api/v1/spots

# создание отзыва
curl -X POST http://localhost:8080/api/v1/spots/1/reviews \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"rating":5,"comment":"Тихо и быстрый Wi‑Fi"}'
```

## Геопоиск и PostGIS
В репозитории реализован геопоиск по радиусу на базе Haversine (lat/lon). Для прод-среды рекомендуется:
- Добавить колонку `location GEOMETRY(Point,4326)` в `spots`.
- Индекс: `CREATE INDEX idx_spots_location ON spots USING GIST (location);`
- Запрос по радиусу: `ST_DWithin(location::geography, ST_MakePoint(:lon,:lat)::geography, :radius)`.
- Подключить Hibernate Spatial и миграции Flyway.

## Тестирование
- Unit: JUnit 5 + Mockito.
- Интеграционные: Spring Boot Test + Testcontainers (PostgreSQL). Конфиг тестов в `src/test/resources/application-test.yml` использует JDBC URL вида `jdbc:tc:postgresql:16:///rms`.

Запуск:
```bash
mvn test
```

## Docker и CI/CD
Сборка образа:
```bash
docker build -t rms-app:latest .
```
Запуск с docker-compose:
```bash
docker compose up -d
```
GitLab CI (`.gitlab-ci.yml`): стадии build → test → docker (сборка и push образа в GitLab Registry). Деплой на VPS/PaaS осуществляется из реестра (пример):
```bash
docker run -d -p 80:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/rms \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e JWT_SECRET=change-me-very-strong \
  --name rms-app my-registry/rms:latest
```

## Roadmap
- Перевод геопоиска на PostGIS + сортировка по расстоянию.
- Средний рейтинг, “топ‑места”, модерирование.
- Фото в отзывах (S3‑совместимое хранилище).
- Swagger/OpenAPI, rate limiting, кэширование (Redis).
