--prompt PL/SQL Developer import file
--prompt Created on 2013年7月30日 by renhui
--set feedback off
--set define off
--First　delete　the　tables　if　they　exist.　
--Ignore　the　table　does　not　exist　error　if　present　
drop table CUSTOM;
drop table SCORE;
drop table COMMON_ORDER;
--prompt Creating CUSTOM...
create table CUSTOM
(
  id     int primary key  not null,
  name   VARCHAR(32),
  age    int
);
--prompt Creating SCORE...
create table SCORE
(
  id      VARCHAR(32) primary key not null,
  name    VARCHAR(32),
  score   int,
  course  VARCHAR(32)
);
create table COMMON_ORDER
(
  id     VARCHAR(100) primary key  not null,
  name   VARCHAR(32),
  age    int
);