CREATE TABLE `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` bigint DEFAULT NULL,
  `applicationId` varchar(20) DEFAULT NULL,
  `tag` varchar(100) DEFAULT NULL,
  `word` varchar(100) DEFAULT NULL,
  `problem_category` smallint(6) DEFAULT NULL,
  `problem_index` smallint(6) DEFAULT NULL,
  `duration` decimal(10,0) DEFAULT NULL,
  `level` varchar(100) DEFAULT NULL,
  `mode` varchar(100) DEFAULT NULL,
  `value` text,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;

