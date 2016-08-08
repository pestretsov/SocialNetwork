DROP VIEW IF EXISTS PostView;
DROP TABLE IF EXISTS "Like";
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Follow;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS User;

CREATE TABLE User (
  id         INT PRIMARY KEY AUTO_INCREMENT,
  username   VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL,
  gender     INT DEFAULT 0,
  birth_date DATE,
  bio        VARCHAR(255),
  role       INT DEFAULT 0
);

CREATE TABLE Post (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  from_id      INT NOT NULL REFERENCES User(id),
  post_type    INT DEFAULT 0,
  text         VARCHAR(255),
  publish_time TIMESTAMP NOT NULL
);

CREATE TABLE Follow (
  follower_id INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id     INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (follower_id, user_id)
);

CREATE TABLE "Like" (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  post_id      INT NOT NULL REFERENCES Post(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id      INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  publish_time TIMESTAMP NOT NULL
);

CREATE TABLE Comment (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  post_id      INT NOT NULL REFERENCES Post(id) ON UPDATE CASCADE ON DELETE CASCADE,
  from_id      INT NOT NULL REFERENCES Post(id) ON UPDATE CASCADE ON DELETE CASCADE,
  publish_time TIMESTAMP NOT NULL
);


CREATE OR REPLACE VIEW PostView (post_id, post_text, post_type, post_publish_time, from_id, from_username, from_first_name, from_last_name)
AS SELECT Post.id, Post.text, Post.post_type, Post.publish_time, Post.from_id, User.username, User.first_name, User.last_name
   FROM Post JOIN User ON Post.from_id=User.id;

INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, GENDER, BIRTH_DATE, BIO, role)
VALUES ('ambush', '4297715b943c507b0bcefe8da5d5fed8165a4b8152e019e6e0af73e18e7ff8b891f9b7eb0432ed6cebbceac8a48f1c68', 'Artemy', 'Pestretsov', '1', '1995-11-02', 'nothing do here', 0);

