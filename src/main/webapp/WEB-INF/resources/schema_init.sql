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
  post_type    INT DEFAULT 0,
  text         TEXT,
  publish_time TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS Follow;
CREATE TABLE Follow (
  follower_id INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (follower_id, user_id)
);

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, BIO)
VALUES ('ambush', '4297715b943c507b0bcefe8da5d5fed8165a4b8152e019e6e0af73e18e7ff8b891f9b7eb0432ed6cebbceac8a48f1c68', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here');

