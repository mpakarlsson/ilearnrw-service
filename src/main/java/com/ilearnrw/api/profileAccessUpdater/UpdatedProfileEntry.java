package com.ilearnrw.api.profileAccessUpdater;

public class UpdatedProfileEntry {
	private int category, index, previousSeverity, newSeverity;

	public UpdatedProfileEntry(int category, int index, int previousSeverity,
			int newSeverity) {
		this.category = category;
		this.index = index;
		this.previousSeverity = previousSeverity;
		this.newSeverity = newSeverity;
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
	
}
