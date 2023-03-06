CREATE TABLE IF NOT EXISTS Users
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS Mpaa
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Genres
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Films
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INTEGER      NOT NULL,
    mpa_rating   INTEGER,
    FOREIGN KEY (mpa_rating) REFERENCES Mpaa (id)
);

CREATE TABLE IF NOT EXISTS Film_Genre
(
    film_id  INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES Films (id),
    FOREIGN KEY (genre_id) REFERENCES Genres (id)
);

CREATE TABLE IF NOT EXISTS User_Friend
(
    user_id   INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES Users (id),
    FOREIGN KEY (friend_id) REFERENCES Users (id)
);

CREATE TABLE IF NOT EXISTS Film_Like
(
    user_id INTEGER NOT NULL,
    film_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES Users (id),
    FOREIGN KEY (film_id) REFERENCES Films (id)
);