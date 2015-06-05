package com.ilearnrw.app.screening;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;

public class ScreeningTestList implements Serializable{
	private static final long serialVersionUID = 1L;
	private String defaultTest;
	private ArrayList<String> filenames;
	public ScreeningTestList() {
		this.defaultTest = null;
		this.filenames = new ArrayList<String>();
	}
	public ScreeningTestList(String defaultTest, ArrayList<String> filenames) {
		this.defaultTest = defaultTest;
		this.filenames = filenames;
	}
	public String getDefaultTest() {
		return defaultTest;
	}
	public void setDefaultTest(String defaultTest) {
		this.defaultTest = defaultTest;
	}
	public ArrayList<String> getFilenames() {
		return filenames;
	}
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
	public void addNewTest(String name){
		if (!filenames.contains(name))
			filenames.add(name);
	}
	public void deleteTest(String name){
		if (filenames.contains(name)){
			int t = filenames.indexOf(name);
			filenames.remove(t);
		}
	}

	public void storeScreeningTestList(String filename) {
		Gson gson = new Gson();
		String jsonRepresentation = gson.toJson(this);
		try {
			FileWriter Filewriter = new FileWriter(filename);
			Filewriter.write(jsonRepresentation);
			Filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadScreeningTestList(String filename) {
		Gson gson = new Gson();
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader buffered = new BufferedReader(fileReader);
			ScreeningTestList obj = gson.fromJson(buffered, ScreeningTestList.class);
			this.defaultTest = obj.getDefaultTest();
			this.filenames = obj.getFilenames();
		} catch (IOException e) {

		}
	}
}
