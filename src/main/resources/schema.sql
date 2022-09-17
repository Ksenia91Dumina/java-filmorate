CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT PRIMARY KEY AUTO_INCREMENT,
    EMAIL     VARCHAR(255) NOT NULL,
    LOGIN     VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
    USER_ID INT NOT NULL,
    FRIEND_ID INT NOT NULL,
    CONSTRAINT PK_FRIENDSHIP PRIMARY KEY (USER_ID, FRIEND_ID),
    CONSTRAINT FK_FRIENDSHIP_1 FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    CONSTRAINT FK_FRIENDSHIP_2 FOREIGN KEY (FRIEND_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME   VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INT PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(200) NOT NULL,
    RELEASE_DATE DATE,
    DURATION     INT,
    MPA_ID       INT,
    CONSTRAINT FK_FILMS FOREIGN KEY (MPA_ID) REFERENCES MPA (MPA_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME     VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INT NOT NULL,
    GENRE_ID INT NOT NULL,
    CONSTRAINT FK_FILM_GENRE_1 FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT FK_FILM_GENRE_2 FOREIGN KEY (GENRE_ID) REFERENCES GENRE (GENRE_ID) ON DELETE CASCADE
);

create table if not exists USERS_LIKES
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null ,
    CONSTRAINT PK_USERS_LIKES PRIMARY KEY (USER_ID, FILM_ID),
    CONSTRAINT FK_USERS_LIKES_FILMS FOREIGN KEY (FILM_ID)  REFERENCES FILMS ON DELETE CASCADE,
    constraint FK_USERS_LIKES_USERS FOREIGN KEY (USER_ID) REFERENCES USERS ON DELETE CASCADE
);


