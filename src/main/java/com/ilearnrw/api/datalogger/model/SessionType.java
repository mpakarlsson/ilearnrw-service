package com.ilearnrw.api.datalogger.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
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
