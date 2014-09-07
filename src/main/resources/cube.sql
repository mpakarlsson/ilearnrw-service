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

