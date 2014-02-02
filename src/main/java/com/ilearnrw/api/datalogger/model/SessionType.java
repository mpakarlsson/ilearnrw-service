package com.ilearnrw.api.datalogger.model;

public enum SessionType {

	LEARN, APPLICATION, ROUND;

	public static char toChar(SessionType type) {
		switch (type) {
		case LEARN:
			return 'L';
		case APPLICATION:
			return 'A';
		case ROUND:
			return 'R';
		}
		return ' ';
	};
}
