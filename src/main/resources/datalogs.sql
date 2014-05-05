CREATE TABLE `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
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

DELIMITER //
CREATE PROCEDURE addLog(IN inUserId VARCHAR(32), IN inTag VARCHAR(32), IN inValue VARCHAR(512), IN inApplicationId VARCHAR(32), IN inSessionId VARCHAR(32))
BEGIN
	INSERT INTO logs(userId,tag,value,applicationId,timestamp,sessionId)VALUES(inUserId,inTag,inValue,inApplicationId,NOW(),inSessionId);
END //
DELIMITER ; //
