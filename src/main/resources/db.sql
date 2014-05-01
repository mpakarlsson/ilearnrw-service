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


CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `ilearnrw`.`applications` (`id`, `name`) VALUES 
(0, 'Mail Sorter'),
(1, 'Whack a Mole'),
(2, 'Endless Runner'),
(3, 'Harvest'),
(4, 'Serenade Hero'),
(5, 'Moving Pathways'),
(6, 'Eye Exam'),
(7, 'Train Dispatcher'),
(8, 'Drop Chops'),
(9, 'Game World'),
(10, 'Social Network'),
(11, 'Logging Screen'),
(12, 'Profile Setup')