SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'),('ROLE_MANAGER'),('ROLE_ADMIN');

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password char(80) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  email varchar(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO users (username,password,first_name,last_name,email) VALUES ('user','$2a$10$rBgcI/4eoaPTqQpcslaewurF8SGmpuiEf356jK8HHj.FfsKWHoaii','Bob','GeekBrains','bob@gb.com'),
('admin','$2a$10$rBgcI/4eoaPTqQpcslaewurF8SGmpuiEf356jK8HHj.FfsKWHoaii','Gregor','GeekBrains','gregor@gb.com');

/*INSERT INTO users (username,password,first_name,last_name,email)
VALUES
('alex','$2a$10$dU./QwrYDBshAavTOWneHOUSPLSNR/bJYLcaGRfeRfVkfiFCRlzHa','Alex','GeekBrains','alex@gb.com');*/

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles (
  user_id int NOT NULL,
  role_id int NOT NULL,

  PRIMARY KEY (user_id, role_id),

  KEY FK_ROLE_idx (role_id),

  CONSTRAINT FK_USER_05 FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
  REFERENCES roles (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(2, 3);

DROP TABLE IF EXISTS books;

CREATE TABLE books (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` text,
  `short_description` text,
  `release_year` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO books (title, short_description, release_year)
VALUES
('Книга1','Описание1',2000),('Книга2','Описание2',2010),('Книга3','Описание3',2020),
('New product 2','Описание',0),('ура','ура',5555),('123','123',123),
('999','999',999),('1010','1010',1010);

SET FOREIGN_KEY_CHECKS = 1;