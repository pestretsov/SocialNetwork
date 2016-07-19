DROP TABLE IF EXISTS User;
CREATE TABLE User (
  id         INT PRIMARY KEY AUTO_INCREMENT,
  username   VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL,
  sex        INT DEFAULT 0,
  birth_date DATE,
  bio        TEXT,
);

DROP TABLE IF EXISTS Post;
CREATE TABLE Post (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  from_id      INT NOT NULL REFERENCES User(id),
  post_type    INT NOT NULL DEFAULT 0,
  text         TEXT,
  publish_time TIMESTAMP NOT NULL
);

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, BIO)
VALUES ('ambush', '123', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here');

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, BIO)
VALUES ('ambush1', '***', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here');

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, BIO)
VALUES ('ambush2', '***', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here');

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, BIO)
VALUES ('ambush3', '***', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here');
