CREATE TABLE users (
  username VARCHAR(50),
  password  VARCHAR(60),
  PRIMARY KEY (username)
);

CREATE TABLE authorities (
  username VARCHAR(50),
  authority  VARCHAR(50),
  PRIMARY KEY (username)
);