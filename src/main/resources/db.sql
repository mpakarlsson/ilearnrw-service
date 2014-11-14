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
  `teacher_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  PRIMARY KEY (`student_id`)
);

CREATE TABLE `experts_teachers` (
  `expert_id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) NOT NULL
);

CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appId` VARCHAR(45) NULL,
  `name` varchar(20) DEFAULT NULL,
  `letters` BIT NULL, 
  `words` BIT NULL, 
  `sentence` BIT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `schools` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
);

CREATE TABLE `classrooms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `school_id` bigint(20) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_school_UNIQUE` (`school_id`,`name`)
);

CREATE TABLE `student_classrooms` (
  `student_id` BIGINT NOT NULL,
  `classroom_id` BIGINT NULL,
  PRIMARY KEY (`student_id`));
  

CREATE VIEW `schools_classrooms` AS 
SELECT `s`.`id` AS `school_id`,`s`.`name` AS `school_name`,`c`.`id` AS `classroom_id`,`c`.`name` AS `classroom_name` 
FROM (`schools` `s` join `classrooms` `c`) 
WHERE (`s`.`id` = `c`.`school_id`);

CREATE VIEW `students` AS
SELECT u.id, u.username AS name
FROM users u, role_members rm, roles r
WHERE u.id = rm.members_id AND rm.roles_id = r.id AND r.name = "ROLE_STUDENT";

CREATE VIEW `student_details` AS
SELECT s.id AS student_id, sch.name AS school, c.name AS classroom, ts.teacher_id
FROM students s 
LEFT JOIN student_classrooms sc ON s.id = sc.student_id
LEFT JOIN teachers_students ts ON s.id = ts.student_id
JOIN classrooms c ON sc.classroom_id = c.id
JOIN schools sch ON sch.id = c.school_id;


SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO';

INSERT INTO `applications` (`id`, `appId`, `name`, `letters`, `words`, `sentence`) VALUES 
(0, 'MAIL_SORTER', 'Mail Sorter', 0, 1, 0),
(1, 'WHAK_A_MOLE', 'Whack a Mole', 0, 1, 0),
(2, 'ENDLESS_RUNNER', 'Endless Runner', 0, 1, 0),
(3, 'HARVEST', 'Harvest', 0, 1, 0),
(4, 'SERENADE_HERO', 'Serenade Hero', 0, 0, 1),
(5, 'MOVING_PATHWAYS', 'Moving Pathways', 1, 1, 0),
(6, 'EYE_EXAM', 'Eye Exam', 1, 0, 1),
(7, 'TRAIN_DISPATCHER', 'Train Dispatcher', 0, 1, 0),
(8, 'DROP_CHOPS', 'Drop Chops', 0, 1, 0),
(9, 'GAME_WORLD', 'Game World', 0, 0, 0),
(10, 'SOCIAL_NETWORK', 'Social Network', 0, 0, 0),
(11, 'LOGGING_SCREEN', 'Logging Screen', 0, 0, 0),
(12, 'PROFILE_SETUP', 'Profile Setup', 0, 0, 0);


CREATE  TABLE `problems` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(200) NULL ,
  `language` VARCHAR(2) NULL ,
  PRIMARY KEY (`id`) );

INSERT INTO `problems` (`title`, `language`) VALUES 
('Syllable Division', 'EN'),
('Vowel Sounds', 'EN'),
('Suffixing', 'EN'),
('Prefixing', 'EN'),
('Phon-Graph', 'EN'),
('Letter Patterns', 'EN'),
('Letter Names', 'EN'),
('Sight Words', 'EN'),
('Confusing Letter Shapes', 'EN'),
('Syllable Division', 'GR'),
('Phonemes:Consonants', 'GR'),
('Phonemes:Vowels', 'GR'),
('Suffixing:Derivational', 'GR'),
('Suffixing:Inflectional/Grammatical', 'GR'),
('Prefixing', 'GR'),
('Grapheme/Phoneme Correspondence', 'GR'),
('Grammar/Function Words', 'GR'),
('Word Recognition', 'GR'),
('Visual Similarity', 'GR');


CREATE  TABLE `apps_problems` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `id_application` INT NULL ,
  `id_problem` INT NULL ,
  PRIMARY KEY (`id`) );

 INSERT INTO `apps_problems`
(`id_application`,`id_problem`)
VALUES
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Vowel Sounds' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Phon-Graph' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Letter Patterns' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Letter Names' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Sight Words' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Vowel Sounds' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Phon-Graph' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Letter Patterns' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Letter Names' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Sight Words' AND language like 'EN')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Letter Patterns' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Vowel Sounds' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Phon-Graph' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Letter Patterns' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Letter Names' AND language like 'EN')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Vowel Sounds' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Phon-Graph' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Letter Patterns' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Sight Words' AND language like 'EN')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Vowel Sounds' AND language like 'EN')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Phon-Graph' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Sight Words' AND language like 'EN')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Confusing Letter Shapes' AND language like 'EN')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Syllable Division' AND language like 'EN')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Suffixing' AND language like 'EN')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Prefixing' AND language like 'EN')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Phonemes:Consonants' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Grapheme/Phoneme Correspondence' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Word Recognition' AND language like 'GR')),
((select id from applications where name like 'Mail Sorter'), (select id from problems where title like 'Visual Similarity' AND language like 'GR')),

((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Grapheme/Phoneme Correspondence' AND language like 'GR')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Word Recognition' AND language like 'GR')),
((select id from applications where name like 'Whack a Mole'), (select id from problems where title like 'Visual Similarity' AND language like 'GR')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Endless Runner'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Phonemes:Vowels' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Grapheme/Phoneme Correspondence' AND language like 'GR')),
((select id from applications where name like 'Harvest'), (select id from problems where title like 'Grammar/Function Words' AND language like 'GR')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Serenade Hero'), (select id from problems where title like 'Grammar/Function Words' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Phonemes:Consonants' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Phonemes:Vowels' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Grapheme/Phoneme Correspondence' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Grammar/Function Words' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Word Recognition' AND language like 'GR')),
((select id from applications where name like 'Moving Pathways'), (select id from problems where title like 'Visual Similarity' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Phonemes:Consonants' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Phonemes:Vowels' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Eye Exam'), (select id from problems where title like 'Word Recognition' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Suffixing:Inflectional/Grammatical' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Prefixing' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Grammar/Function Words' AND language like 'GR')),
((select id from applications where name like 'Train Dispatcher'), (select id from problems where title like 'Word Recognition' AND language like 'GR')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Syllable Division' AND language like 'GR')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Suffixing:Derivational' AND language like 'GR')),
((select id from applications where name like 'Drop Chops'), (select id from problems where title like 'Prefixing' AND language like 'GR'))
;


