CREATE TABLE IF NOT EXISTS USERS(
   USER_ID INT PRIMARY KEY AUTO_INCREMENT,
   EMAIL VARCHAR(255) NOT NULL,
    LOGIN VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    BIRTHDAY DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS FRIENDSHIP(
  ID INT PRIMARY KEY AUTO_INCREMENT,
  USER1_ID INT,
  USER2_ID INT,
  CONSTRAINT FK_FRIENDSHIP_1 FOREIGN KEY (USER1_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT FK_FRIENDSHIP_2 FOREIGN KEY (USER2_ID) REFERENCES USERS (USER_ID),
    STATUS BOOLEAN
);

CREATE TABLE IF NOT EXISTS RAITING_MPA(
  MPA_ID INT PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS(
    FILM_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(200) NOT NULL,
    RELEASE_DATE DATE,
    DURATION INT,
    MPA_ID INT,
    CONSTRAINT FK_FILMS FOREIGN KEY (MPA_ID) REFERENCES RAITING_MPA (MPA_ID)
    );

CREATE TABLE IF NOT EXISTS GENRE(
    GENRE_ID INT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_GENRE(
    FILM_ID INT,
    GENRE_ID INT,
    CONSTRAINT FK_FILM_GENRE_1 FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID),
    CONSTRAINT FK_FILM_GENRE_2 FOREIGN KEY (GENRE_ID) REFERENCES GENRE (GENRE_ID)
    );


