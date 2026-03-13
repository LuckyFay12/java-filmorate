# java-filmorate
Данный проект представляет собой бекенд веб-сервиса для работы с фильмами, пользователями и отзывами, а также для управления связями между ними. Основной целью этого проекта является предоставление пользователю возможности удобно и эффективно управлять информацией о фильмах, обмениваться отзывами и рекомендациями с другими пользователями, а также находить интересные фильмы на основе их предпочтений.

### Реализованы следующие эндпоинты:

### Фильмы
POST /films - создание фильма

PUT /films - редактирование фильма

GET /films - получение списка всех фильмов

GET /films/{id} - получение информации о фильме по его id

PUT /films/{id}/like/{userId} — поставить лайк фильму

DELETE /films/{id}/like/{userId} — удалить лайк фильма

DELETE /films/{id} - удаление фильма по id

GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10.

GET /films/search?query={query}?by={by} - поиск фильмов по заголовку и режиссеру

GET /films/director/directorId={directorId}?sortBy={sortBy} - получение всех фильмов режиссера с сортировкой по лайкам или годам

GET /films/common?userId={userId}?friendId={friendId} - получение общих фильмов пользователя и его друга

### Пользователи
POST /users - создание пользователя

PUT /users - редактирование пользователя

GET /users - получение списка всех пользователей

DELETE /users/{userId} - удаление пользователя по id

GET /users/{id} - получение данных о пользователе по id

PUT /users/{id}/friends/{friendId} — добавление в друзья

DELETE /users/{id}/friends/{friendId} — удаление из друзей

GET /users/{id}/friends — возвращает список друзей

GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

GET /users/{id}/recommendations - получение рекомендаций по фильмам

GET /users/{id}/feed - возвращает ленту событий пользователя

### Режиссеры
POST /directors - создание режиссера

GET /directors - получение списка всех режиссеров

GET /directors/{id} - получение режиссера по id

PUT /directors - обновление режиссера

DELETE /directors/{id} - удаление режиссера по id

### Жанры
GET /genres - получение списка всех жанров

GET /genres/{id} - получение жанра по id

### MPA рейтинг
GET /mpa - получение списка всех рейтингов

GET /mpa/{id} - получение рейтинга по id

### Отзывы
POST /reviews - создание отзыва

PUT /reviews - обновление отзыва

DELETE /reviews/{id} - удаление отзыва по id

GET /reviews/{id} - получение отзыва по id

GET /reviews?filmId={filmId}?count={count} - получение count отзывов по id фильма. Если значение не задано, возвращает первые 10

### Лайки
PUT /reviews/{id}/like/{userId} - добавление лайка

PUT /reviews/{id}/dislike/{userId} - добавление дизлайка

DELETE /reviews/{id}/like{userId} - удаление лайка

DELETE /reviews{id}/dislike{userId} - удаление дизлайка