
use test;
SET FOREIGN_KEY_CHECKS=0;
drop table if exists vote;
drop table if exists answer;
drop table if exists user;
drop table if exists question;
SET FOREIGN_KEY_CHECKS=1;


CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(100),
    password VARCHAR(100) );

CREATE TABLE question (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    text VARCHAR(5000),
    FOREIGN KEY (user_id) REFERENCES user(id) );

CREATE TABLE answer (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    question_id INT,
    text VARCHAR(5000),
    FOREIGN KEY (user_id) REFERENCES user(id) );


CREATE TABLE vote (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    value INT,
    user_id INT,
    question_id INT,
    answer_id INT,
    direction VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES user(id),
	FOREIGN KEY (question_id) REFERENCES question(id) ,
	FOREIGN KEY (answer_id) REFERENCES answer(id) );


-- INSERT INTO user (name, email, role, password) VALUES('cornel', 'cornelao@gmail.com', 'USER', '12345')