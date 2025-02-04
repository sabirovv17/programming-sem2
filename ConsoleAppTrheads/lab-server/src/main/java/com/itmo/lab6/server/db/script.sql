--dropping tables and enums if u need it
drop table if exists coordinates cascade;
drop type if exists color cascade;
drop table if exists location cascade;
drop table if exists person cascade;

-- coordinates table
create table coordinates(
    coordinates_id serial primary key,
    coord_x float8 NOT NULL check(coord_x >= -442), 
    coord_y int NOT NULL check(coord_y >= -258)
);
-- color enum
create type color as enum('RED', 'BLACK', 'BLUE', 'WHITE', 'BROWN');
-- location table
create table location(
    location_id serial primary key, 
    x float NOT NULL, 
    y float8 NOT NULL,
    z int8 NOT NULL
);
--для уникальности person_id и чтобы он был long, а не int(ну типа занимал не 4 а 8 байт памяти)
CREATE SEQUENCE person_id_seq START WITH 1 INCREMENT BY 1;

--users table
create table users(
    user_id serial primary key, 
    login varchar(64) not null, 
    password text not null 
);

create table person(
    person_id BIGINT PRIMARY KEY DEFAULT nextval('person_id_seq'),    
    person_name text NOT NULL, 
    coordinates_id int NOT NULL references coordinates(coordinates_id), 
    creationDate timestamp default CURRENT_TIMESTAMP , 
    height int8 not null CHECK(height >= 0), 
    weight int8 not null check(weight >= 0), 
    passportID varchar(34) not null check(LENGTH(passportID) between 5 and 34), 
    color color not null, 
    location_id int not null references location(location_id), 
    user_id int not null references users(user_id)
);


--тестовые значения для бд
-- Добавляем данные в таблицу coordinates
INSERT INTO coordinates (coord_x, coord_y) VALUES (50.5, 40.7128);

-- Добавляем данные в таблицу location
INSERT INTO location (x, y, z) VALUES (10.0, 20.0, 30);

-- Добавляем данные в таблицу users
INSERT INTO users (login, password) VALUES ('test_user', 'test_password');

-- Добавляем данные в таблицу person
INSERT INTO person (person_name, coordinates_id, height, weight, passportID, color, location_id, user_id) 
VALUES ('John', 1, 180, 80, 'AB12345678', 'BLUE', 1, 1);
