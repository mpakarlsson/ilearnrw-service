delimiter $$

CREATE TABLE `facts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user_ref` int(11) DEFAULT NULL,
  `app_ref` int(11) DEFAULT NULL,
  `problem_ref` int(11) DEFAULT NULL,
  `word` varchar(100) DEFAULT NULL,
  `word_status` varchar(100) DEFAULT NULL,
  `duration` decimal(10,0) DEFAULT NULL,
  `learn_session_ref` int(11) DEFAULT NULL,
  `app_session_ref` int(11) DEFAULT NULL,
  `app_round_session_ref` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci$$
delimiter $$

CREATE TABLE `problems` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` smallint(6) DEFAULT NULL,
  `idx` smallint(6) DEFAULT NULL,
  `language` smallint(6) DEFAULT NULL,
  `description` text character set utf8 collate utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci$$

delimiter $$

CREATE TABLE `sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sessiontype` char(1) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `start` timestamp NULL DEFAULT NULL,
  `end` timestamp NULL DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `supervisor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci$$

delimiter $$

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `birthyear` smallint(6) DEFAULT NULL,
  `language` char(2) DEFAULT NULL,
  `classroom` varchar(45) DEFAULT NULL,
  `school` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci$$

delimiter $$

CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci$$

delimiter ; $$

CREATE VIEW `facts_expanded` AS
select f.*,
	u.username, u.gender, u.birthyear, u.language, u.school, u.classroom,
	a.name as app_name, a.app_id,
	p.category, p.idx, p.language as p_language, p.description,
	aps.name as aps_name, aps.start as aps_start, aps.end as aps_end,
	lrs.name as lrs_name, lrs.start as lrs_start, lrs.end as lrs_end,
	rds.name as rds_name, rds.start as rds_start, rds.end as rds_end
from facts f
left join users u on u.id = f.user_ref
left join applications a on a.id = f.app_ref
left join problems p on p.id = f.problem_ref
left join sessions aps on aps.id = f.app_session_ref and aps.sessiontype = 'A'
left join sessions lrs on lrs.id = f.learn_session_ref and lrs.sessiontype = 'L'
left join sessions rds on rds.id = f.app_round_session_ref and rds.sessiontype = 'R';
