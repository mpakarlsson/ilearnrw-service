package com.ilearnrw.app.screening;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

public class DefaultTests {
	private HashMap<String, String> tests;

	public DefaultTests() {
		this.tests = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getTests() {
		return tests;
	}


	public void setTests(HashMap<String, String> tests) {
		this.tests = tests;
	}

	public void setTestName(String language, String fname){
		tests.put(language, fname);
	}

	public String getTestName(String language){
		return tests.get(language);
	}

	public void storeDefault(String filename) {
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

	public void loadDefault(String filename) {
		Gson gson = new Gson();
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader buffered = new BufferedReader(fileReader);
			DefaultTests obj = gson.fromJson(buffered, DefaultTests.class);
			this.tests = obj.getTests();
		} catch (IOException e) {

		}
	}
}
