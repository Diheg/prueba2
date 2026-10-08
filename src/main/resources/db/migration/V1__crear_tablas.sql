CREATE TABLE author (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE book (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(150) NOT NULL,
    isbn      VARCHAR(20)  NOT NULL UNIQUE,
    genre     VARCHAR(50)  NOT NULL,
    author_id BIGINT       NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author (id)
);
