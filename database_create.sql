CREATE TYPE question_type as ENUM('single', 'multiple');

CREATE TYPE role_type as ENUM('user', 'admin');

CREATE TABLE categories
(
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE surveys
(
    id SERIAL PRIMARY KEY,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    created_at timestamp NOT NULL
);

CREATE TABLE questions
(
    id SERIAL PRIMARY KEY,
    survey_id INT NOT NULL,
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    text VARCHAR(1000) NOT NULL,
    type question_type NOT NULL
);

CREATE TABLE options
(
    id serial PRIMARY KEY,
    question_id INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    description VARCHAR(1000) NOT NULL
);

CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles
(
    user_id SERIAL PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users(id),
    role role_type NOT NULL
);

CREATE TABLE sessions
(
    id SERIAL PRIMARY KEY,
    survey_id INT NOT NULL,
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    started_at timestamp NOT NULL,
    finished_at timestamp
);

CREATE TABLE answers
(
    session_id INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    option_id INT NOT NULL,
    FOREIGN KEY (option_id) REFERENCES options(id),
    CONSTRAINT answers_pk PRIMARY KEY (session_id, option_id)
);