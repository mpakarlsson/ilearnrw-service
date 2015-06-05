package com.ilearnrw.api.profileAccessUpdater;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class UpdatedProfileEntry {
	private int category, index, previousSeverity, newSeverity, previousWorkingIndex, newWorkingIndex;

	public UpdatedProfileEntry(int category, int index, int previousSeverity,
			int newSeverity, int previousWorkingIndex, int newWorkingIndex) {
		this.category = category;
		this.index = index;
		this.previousSeverity = previousSeverity;
		this.newSeverity = newSeverity;
		this.previousWorkingIndex = previousWorkingIndex;
		this.newWorkingIndex = newWorkingIndex;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPreviousSeverity() {
		return previousSeverity;
	}

	public void setPreviousSeverity(int previousSeverity) {
		this.previousSeverity = previousSeverity;
	}

	public int getNewSeverity() {
		return newSeverity;
	}

	public void setNewSeverity(int newSeverity) {
		this.newSeverity = newSeverity;
	}

	public int getPreviousWorkingIndex() {
		return previousWorkingIndex;
	}

	public void setPreviousWorkingIndex(int previousWorkingIndex) {
		this.previousWorkingIndex = previousWorkingIndex;
	}

	public int getNewWorkingIndex() {
		return newWorkingIndex;
	}

	public void setNewWorkingIndex(int newWorkingIndex) {
		this.newWorkingIndex = newWorkingIndex;
	}
	
}
