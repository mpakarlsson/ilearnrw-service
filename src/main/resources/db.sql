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
  `letters` BIT NULL, 
  `words` BIT NULL, 
  `sentence` BIT NULL
  PRIMARY KEY (`id`)
);

SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO';

INSERT INTO `ilearnrw`.`applications` (`id`, `name`, `letters`, `words`, `sentence`) VALUES 
(0, 'Mail Sorter', 0, 1, 0),
(1, 'Whack a Mole', 0, 1, 0),
(2, 'Endless Runner', 0, 1, 0),
(3, 'Harvest', 0, 1, 0),
(4, 'Serenade Hero', 0, 0, 1),
(5, 'Moving Pathways', 1, 1, 0),
(6, 'Eye Exam', 1, 0, 1),
(7, 'Train Dispatcher', 0, 1, 0),
(8, 'Drop Chops', 0, 1, 0),
(9, 'Game World', 0, 0, 0),
(10, 'Social Network', 0, 0, 0),
(11, 'Logging Screen', 0, 0, 0),
(12, 'Profile Setup', 0, 0, 0);


CREATE  TABLE `ilearnrw`.`problems` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(200) NULL ,
  `language` VARCHAR(2) NULL ,
  PRIMARY KEY (`id`) );

INSERT INTO `ilearnrw`.`problems` (`title`, `language`) VALUES 
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


CREATE  TABLE `ilearnrw`.`apps_problems` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `id_application` INT NULL ,
  `id_problem` INT NULL ,
  PRIMARY KEY (`id`) );

 INSERT INTO `ilearnrw`.`apps_problems`
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


