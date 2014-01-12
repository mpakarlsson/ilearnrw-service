create table users(
  id bigint AUTO_INCREMENT not null primary key,
  username varchar(100) not null,
  password varchar(100) not null,
  enabled boolean not null);
                  
create table roles(
  id bigint AUTO_INCREMENT not null primary key,
  name varchar(100) not null);
  
create table permissions(
  id bigint AUTO_INCREMENT not null primary key,
  name varchar(100) not null);
  
create table role_members(
  roles_id bigint not null,
  members_id bigint not null);

create table role_permissions(
  roles_id bigint not null,
  permissions_id bigint not null);
  
  
INSERT INTO `ilearnrw`.`users` (`username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES ('joe_t', 'test', 1, 'M', '2008-05-01', 'EN');

INSERT INTO `ilearnrw`.`users` (`username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES ('sue_t', 'test', 1, 'F', '2009-04-18', 'EN');

