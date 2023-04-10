-- SQL Schema for creating POSTGRESQL tables
-- WARNING : this is only a preview of what we can have, not the real deal !
-- Use the scripts in the specialized repository instead :  https://github.com/M2-CYU-Real-Estate/DataStorage

DROP TABLE IF EXISTS city_score;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS estate;
DROP TABLE IF EXISTS estate_price;
DROP TABLE IF EXISTS user_entity;
DROP TABLE IF EXISTS user_navigation;
DROP TABLE IF EXISTS selling_entry;
DROP TABLE IF EXISTS buying_entry;
DROP TABLE IF EXISTS buying_criterion_entry;
DROP TABLE IF EXISTS buying_profile_entry;

CREATE TABLE city_score(
    postal_code text,
    city_name text,
    global_score text,
    security_score text,
    education_score text,
    sport_score text,
    nature_score text,
    praticity_score text
);

CREATE TABLE city(
    city_name text,
    simple_city_name text,
    department text,
    postal_code text,
    population int,
    position point
);

CREATE TABLE estate(
    id SERIAL PRIMARY KEY,
    title text,
    url text,
    ref text,
    type_bien text,
    ville text,
    price INT,
    surface INT,
    pieces INT,
    chambres INT,
    parking INT,
    description text,
    image text,
    dt timestamp
);

CREATE TABLE estate_price(
    id text,
    date timestamp,
    price int
);

CREATE TABLE user_entity(
    id SERIAL PRIMARY KEY,
    name text NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    role text NOT NULL,
    creation_date timestamp NOT NULL,
    last_login_date timestamp NOT NULL,
    last_password_reset_date timestamp
);

CREATE TABLE user_navigation(
    id text,
    title text,
    dt timestamp
);

CREATE TABLE selling_entry(
    id_entry SERIAL PRIMARY KEY,
    name text,
    user_id int,
    city_name text,
    house_features json,
    estimated_price int
);

CREATE TABLE buying_entry(
    id_user int,
    id SERIAL PRIMARY KEY
);

CREATE TABLE buying_criterion_entry(
    id INT,
    id_entry SERIAL PRIMARY KEY,
    criterion json
);

CREATE TABLE buying_profile_entry(
    id INT,
    id_entry SERIAL PRIMARY KEY,
    profile json
);
