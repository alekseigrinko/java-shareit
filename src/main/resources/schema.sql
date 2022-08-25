CREATE TABLE IF NOT EXISTS USERS
(
    ID   INT NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL ,
    EMAIL VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ID   INT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL ,
    DESCRIPTION VARCHAR(512) NOT NULL,
    IS_AVAILABLE BOOLEAN NOT NULL,
    OWNER_ID INT NOT NULL REFERENCES USERS(ID)
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    ID   INT PRIMARY KEY,
    START_DATE DATE NOT NULL ,
    END_DATE DATE NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    ITEM_ID INT NOT NULL REFERENCES ITEMS(ID),
    BOOKER_ID INT NOT NULL REFERENCES USERS(ID),
    STATUS VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    ID   INT PRIMARY KEY,
    TEXT VARCHAR (255) NOT NULL,
    ITEM_ID INT NOT NULL REFERENCES ITEMS(ID),
    AUTHOR_ID INT NOT NULL REFERENCES USERS(ID)
);