create table users(
  id bigint AUTO_INCREMENT not null primary key,
  username varchar(100) not null,
  password varchar(100) not null,
  enabled boolean not null,
  `gender` char(1) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `language` varchar(2) DEFAULT NULL);
                  
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
  

CREATE TABLE `teachers_students` (
  `teacher_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  PRIMARY KEY (`student_id`,`teacher_id`)
);


INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (1, 'admin', 'admin', 1, 'M', '2014-01-01', 'EN');
INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (2, 'joe_t', 'test', 1, 'M', '2008-05-01', 'EN');
INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (3, 'sue_t', 'test', 1, 'F', '2009-04-18', 'EN');
INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (4, 'chris_t', 'test', 1, 'M', '2007-04-21', 'GR');
INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (5, 'xena_t', 'test', 1, 'F', '2009-09-11', 'GR');
INSERT INTO `ilearnrw`.`users` (`id`, `username`, `password`, `enabled`, `gender`, `birthdate`, `language`) VALUES (6, 'teacher', 'test', 1, 'M', '2014-01-01', 'EN');

INSERT INTO `ilearnrw`.`roles` (`id`, `name`) VALUES (1, 'ROLE_STUDENT');
INSERT INTO `ilearnrw`.`roles` (`id`, `name`) VALUES (2, 'ROLE_ADMIN');
INSERT INTO `ilearnrw`.`roles` (`id`, `name`) VALUES (3, 'ROLE_TEACHER');


INSERT INTO `ilearnrw`.`permissions` (`id`, `name`) VALUES (1, 'DEFAULT');

INSERT INTO `ilearnrw`.`role_permissions` (`roles_id`, `permissions_id`) VALUES (1,1);
INSERT INTO `ilearnrw`.`role_permissions` (`roles_id`, `permissions_id`) VALUES (2,1);
INSERT INTO `ilearnrw`.`role_permissions` (`roles_id`, `permissions_id`) VALUES (3,1);


INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (2,1);
INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (1,2);
INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (1,3);
INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (1,4);
INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (1,5);
INSERT INTO `ilearnrw`.`role_members` (`roles_id`, `members_id`) VALUES (3,6);
