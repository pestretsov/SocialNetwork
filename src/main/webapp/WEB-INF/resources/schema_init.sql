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

DROP TABLE IF EXISTS Follower;
CREATE TABLE Follower (
  user_id INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  follower_id INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (user_id, follower_id)
);