CREATE TABLE users(
   id SERIAL PRIMARY KEY,
   username VARCHAR(40),
   first_name VARCHAR(40),
   last_name VARCHAR(40),
   password VARCHAR(255),
   email VARCHAR(255)
);

CREATE TABLE roles(
   id SERIAL PRIMARY KEY,
   name VARCHAR(60)
);

CREATE TABLE user_roles(
    user_id INT,
    role_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (role_id) REFERENCES Roles(id)
);

CREATE TABLE shapes(
   id SERIAL PRIMARY KEY,
   type VARCHAR(40),
   created_at TIMESTAMP,
   last_modified_at TIMESTAMP,
   last_modified_by VARCHAR(40),
   width FLOAT,
   height FLOAT,
   radius FLOAT,
   version INT,
   user_id INT,
   FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE changes(
   id SERIAL PRIMARY KEY,
   shape_id INT,
   last_modified_at TIMESTAMP,
   last_modified_by VARCHAR(40)
);

CREATE TABLE change_authors(
   change_id INT,
   author VARCHAR(60),
   FOREIGN KEY (change_id) REFERENCES Changes(id)
);

CREATE TABLE change_properties(
   change_id INT,
   num_value FLOAT,
   name VARCHAR(80),
   FOREIGN KEY (change_id) REFERENCES Changes(id)
);