CREATE SCHEMA IF NOT EXISTS db_test;

CREATE TABLE IF NOT EXISTS t_user (user_id INT NOT NULL AUTO_INCREMENT, user_name VARCHAR(50) NOT NULL ,age INT not NULL, PRIMARY KEY (user_id));
CREATE TABLE IF NOT EXISTS t_function (function_id INT NOT NULL, function_name VARCHAR(32), function_code VARCHAR(50),function_index INT, PRIMARY KEY (function_id));
CREATE TABLE IF NOT EXISTS t_user_function (id INT NOT NULL, user_id INT NOT NULL, function_id INT NOT NULL, PRIMARY KEY (id));

