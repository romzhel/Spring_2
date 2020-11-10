/*--инициализация отдельной базы для хранения логов---
DROP SCHEMA IF EXISTS logs_db;
CREATE SCHEMA logs_db;
USE logs_db;

DROP TABLE IF EXISTS logs;

CREATE TABLE logs (
  id MEDIUMINT NOT NULL AUTO_INCREMENT,
  event_date DATETIME NOT NULL,
  level VARCHAR(5) NULL,
  logger VARCHAR(255) NULL,
  message TEXT NULL,
  throwable VARCHAR(1000) NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
*/

/*---инициализация основной базы---
CREATE SCHEMA IF NOT EXISTS emarket_db;
USE emarket_db;
*/

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  username              VARCHAR(50) NOT NULL,
  password              CHAR(80) NOT NULL,
  first_name            VARCHAR(50) NOT NULL,
  last_name             VARCHAR(50) NOT NULL,
  email                 VARCHAR(50) NOT NULL,
  phone                 VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  name                  VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles (
  user_id               INT(11) NOT NULL,
  role_id               INT(11) NOT NULL,

  PRIMARY KEY (user_id,role_id),

--  KEY FK_ROLE_idx (role_id),

  CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE CASCADE ON UPDATE NO ACTION,

  CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
  REFERENCES roles (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  title                 VARCHAR(255) NOT NULL,
  description           VARCHAR(5000),
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  category_id           INT(11) NOT NULL,
  vendor_code           VARCHAR(8) NOT NULL,
  title                 VARCHAR(255) NOT NULL,
  short_description     VARCHAR(1000) NOT NULL,
  full_description      VARCHAR(5000) NOT NULL,
  price                 DECIMAL(8,2) NOT NULL,
  create_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id)
  REFERENCES categories (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS products_images;

CREATE TABLE products_images (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  product_id            INT(11) NOT NULL,
  path                  VARCHAR(250) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_PRODUCT_ID_IMG FOREIGN KEY (product_id)
  REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders_statuses;

CREATE TABLE orders_statuses (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  title                 VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS delivery_addresses;

CREATE TABLE delivery_addresses (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  user_id               INT(11) NOT NULL,
  address               VARCHAR(500) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_ID_DEL_ADR FOREIGN KEY (user_id)
  REFERENCES users (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  user_id               INT(11) NOT NULL,
  price                 DECIMAL(8,2) NOT NULL,
  delivery_price        DECIMAL(8,2) NOT NULL,
  delivery_address_id   INT(11) NOT NULL,
  phone_number          VARCHAR(20) NOT NULL,
  status_id             INT(11) NOT NULL,
  delivery_date         TIMESTAMP NOT NULL,
  create_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_ID FOREIGN KEY (user_id)
  REFERENCES users (id),
  CONSTRAINT FK_STATUS_ID FOREIGN KEY (status_id)
  REFERENCES orders_statuses (id),
  CONSTRAINT FK_DELIVERY_ADDRESS_ID FOREIGN KEY (delivery_address_id)
  REFERENCES delivery_addresses (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders_item;

CREATE TABLE orders_item (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  product_id            INT(11) NOT NULL,
  order_id              INT(11) NOT NULL,
  quantity              INT NOT NULL,
  item_price            DECIMAL(8,2) NOT NULL,
  total_price           DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ORDER_ID FOREIGN KEY (order_id)
  REFERENCES orders (id),
  CONSTRAINT FK_PRODUCT_ID_ORD_IT FOREIGN KEY (product_id)
  REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username,password,first_name,last_name,email,phone)
VALUES
('admin','$2a$10$4p6U8Ve1ZjJ/S0Qd9RFyB.hJjpusgdYmTtIIqpHs3k0hfbhDe6cyq','Admin','Admin','admin@gmail.com','+79881111111');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3);

INSERT INTO `categories` (title, description)
VALUES
('Телевизоры','Разные телевизоры'),
('Ноутбуки',NULL),
('Пылесосы','Домашние');

INSERT INTO orders_statuses (title)
VALUES
("Комплектуется"), ("Отправлен");

INSERT INTO products (category_id, vendor_code, title, short_description, full_description, price)
VALUES
(1, "00000001", "20\" Телевизор Samsung UE20NU7170U", "Коротко: Хороший телевизор Samsung 20", "LED телевизор Samsung 20", 1.00),
(1, "00000002", "24\" Телевизор Samsung UE24NU7170U", "Коротко: Хороший телевизор Samsung 24", "LED телевизор Samsung 24", 2.00),
(1, "00000003", "28\" Телевизор Samsung UE28NU7170U", "Коротко: Хороший телевизор Samsung 28", "LED телевизор Samsung 28", 3.00),
(1, "00000004", "32\" Телевизор Samsung UE32NU7170U", "Коротко: Хороший телевизор Samsung 32", "LED телевизор Samsung 32", 4.00),
(1, "00000005", "36\" Телевизор Samsung UE36NU7170U", "Коротко: Хороший телевизор Samsung 36", "LED телевизор Samsung 36", 5.00),
(1, "00000006", "40\" Телевизор Samsung UE40NU7170U", "Коротко: Хороший телевизор Samsung 40", "LED телевизор Samsung 40", 6.00),
(1, "00000007", "44\" Телевизор Samsung UE44NU7170U", "Коротко: Хороший телевизор Samsung 44", "LED телевизор Samsung 44", 7.00),
(1, "00000008", "48\" Телевизор Samsung UE48NU7170U", "Коротко: Хороший телевизор Samsung 48", "LED телевизор Samsung 48", 8.00),
(1, "00000009", "52\" Телевизор Samsung UE52NU7170U", "Коротко: Хороший телевизор Samsung 52", "LED телевизор Samsung 52", 9.00),
(1, "00000010", "56\" Телевизор Samsung UE56NU7170U", "Коротко: Хороший телевизор Samsung 56", "LED телевизор Samsung 56", 10.00),
(1, "00000011", "60\" Телевизор Samsung UE60NU7170U", "Коротко: Хороший телевизор Samsung 60", "LED телевизор Samsung 60", 11.00),
(1, "00000012", "64\" Телевизор Samsung UE64NU7170U", "Коротко: Хороший телевизор Samsung 64", "LED телевизор Samsung 64", 12.00);


INSERT INTO `products_images` (product_id, path)
VALUES
(2,'2.jpg'),
(1,'21380ee9-1499-496a-a43e-fd3234632bdctv4.jpg'),
(3,'1988596c-a1e0-45d6-aa34-3b3ce445cc04tv3.jpg'),
(4,'43a4faa1-6991-4a28-9ff3-fcadcaee26a9tv4.jpg'),
(5,'34266229-004e-40ce-8b9e-79135fbe4ea3tv5.jpg'),
(6,'a2ab4c08-26ec-4a99-bb46-db4bfaaae371tv1.jpg'),
(7,'7b4728f4-d3e7-4520-bf0f-b51d1ab6c5abtv2.jpg'),
(8,'72257efe-cfeb-461e-9afa-82f1ccd6d8d0tv3.jpg'),
(9,'5dbd7dab-3f8f-4be3-86f5-a3b3ec9b534btv4.jpg'),
(10,'061466b7-f0c5-4916-9f76-8bbc29c0b2d0tv5.jpg'),
(11,'ced4a7cb-7825-4f65-8748-4b0e48b6a664tv1.jpg'),
(12,'82657842-dbbc-4dbf-9f05-733c092779cftv2.jpg');

INSERT INTO delivery_addresses (user_id, address)
VALUES
(1, "18a Diagon Alley"),
(1, "4 Privet Drive");