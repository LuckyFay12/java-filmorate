
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    login VARCHAR(100) NOT NULL CHECK (LENGTH(login) >= 5),
    email VARCHAR(255),
    birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    mpa_id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200),
    release_date TIMESTAMP,
    duration INTEGER NOT NULL CHECK (duration > 0),
    mpa_id BIGINT,
    FOREIGN KEY (mpa_id) REFERENCES mpa_ratings(mpa_id),
    CONSTRAINT release_date CHECK (release_date >= '1895-12-28')
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS directors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS film_directors (
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    director_id BIGINT REFERENCES directors(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, friend_id),
    CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

