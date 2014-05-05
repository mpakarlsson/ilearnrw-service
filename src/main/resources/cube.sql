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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=latin1$$
delimiter $$

CREATE TABLE `problems` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` smallint(6) DEFAULT NULL,
  `idx` smallint(6) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `sessions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sessiontype` char(1) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `start` timestamp NULL DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1$$

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1$$


delimiter $$

CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COMMENT='	'$$

delimiter ; $$

