MERGE INTO genres(genre_id, name) KEY(genre_id)
VALUES (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');

MERGE INTO mpa_ratings(mpa_id, name) KEY(mpa_id)
VALUES (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');
