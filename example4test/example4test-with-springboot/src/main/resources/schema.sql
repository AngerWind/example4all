CREATE TABLE IF NOT EXISTS widget
(
    id          INTEGER      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL,
    version     INTEGER      NOT NULL,
    PRIMARY KEY (id)
);