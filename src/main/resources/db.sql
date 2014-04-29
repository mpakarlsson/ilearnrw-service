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
(1, 'Profile Setup'),
(2, 'Whack a Mole'),
(3, 'Endless Runner'),
(4, 'Harvest'),
(5, 'Serenade Hero'),
(6, 'Moving Pathways'),
(7, 'Eye Exam'),
(8, 'Train Dispatcher'),
(9, 'Drop Chops'),
(10, 'Game World'),
(11, 'Social Network'),
(12, 'Logging Screen'),
(13, 'Mail Sorter')