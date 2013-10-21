CREATE TABLE logs (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	userId VARCHAR(32),
	tag VARCHAR(32),
	value VARCHAR(512),
	applicationId VARCHAR(32),
	timestamp DATETIME,
	sessionId VARCHAR(32)
);

DELIMITER //
CREATE PROCEDURE addLog(IN inUserId VARCHAR(32), IN inTag VARCHAR(32), IN inValue VARCHAR(512), IN inApplicationId VARCHAR(32), IN inSessionId VARCHAR(32))
BEGIN
	INSERT INTO logs(userId,tag,value,applicationId,timestamp,sessionId)VALUES(inUserId,inTag,inValue,inApplicationId,NOW(),inSessionId);
END //
DELIMITER;



DELIMITER //
CREATE PROCEDURE selectLogs_(IN inUserId VARCHAR(32), IN inTimeStart DATETIME, IN inTimeEnd DATETIME, IN inLimitStart INTEGER, IN inLimitEnd INTEGER)
BEGIN
	SELECT * FROM logs WHERE userId=inUserId AND timestamp BETWEEN inTimeStart AND inTimeEnd LIMIT inLimitStart, inLimitEnd;
END //
DELIMITER;