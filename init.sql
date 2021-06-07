CREATE DATABASE IF NOT EXISTS authdemo
    COLLATE utf8_general_ci;

USE authdemo

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id             BIGINT(20)  	NOT NULL AUTO_INCREMENT,
    name           VARCHAR(256) NULL UNIQUE,
    password       VARCHAR(256) NULL,
    email          VARCHAR(256) NULL,
    regDate        TIMESTAMP   	DEFAULT CURRENT_TIMESTAMP,
    lastLogin      TIMESTAMP   	NULL,
    banned         BIT(1)      	NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

SET NAMES utf8;

INSERT INTO users(name, password, email, banned)
VALUES ('Admin', '$2y$12$oI7srJeiKn3ll3Oy7IwUHesKijXF7jGtVClJmX4Uf0jA.unY.sr5e', "qwerty@gmail.com", false);
