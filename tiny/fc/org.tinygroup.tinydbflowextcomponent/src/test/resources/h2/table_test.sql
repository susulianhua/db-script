CREATE TABLE If Not Exists  tsys_user (
  user_id varchar(32),
  branch_code varchar(16),
  dep_code varchar(16) ,
  user_name varchar(32) NOT NULL,
  user_pwd varchar(32) NOT NULL
);
CREATE TABLE IF NOT EXISTS sequence (
  name VARCHAR(50) NOT NULL,
  value BIGINT NOT NULL,
  min_value BIGINT NOT NULL,
  max_value BIGINT NOT NULL,
  step INT NOT NULL,
  gmt_create DATE NULL,
  gmt_modified DATE NULL
);
CREATE TABLE If Not Exists  tsys_custom (
  cust_id INT IDENTITY NOT NULL,
  cust_code varchar(16) ,
  cust_name varchar(32) NOT NULL,
  gmt_create DATE NULL
);