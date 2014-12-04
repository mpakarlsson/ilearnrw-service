package com.ilearnrw.api.selectnextactivity.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class NodeTerminal extends Node {
	
	List<Recommendation> recommendation;
	
	public NodeTerminal(String line){
		
		String[] recommendations = line.replace("Terminal,", "").split(",");
    	this.attribute = "Preference";
    	this.ID = Integer.valueOf(recommendations[0]);

		recommendation = new ArrayList<Recommendation>();
		for(int i = 1; i< recommendations.length;i++){
			recommendation.add(new Recommendation(recommendations[i]));
			
		}

	}
	
	public String toString(){
		
		String out = "Terminal,"+ID;
		
		for(Recommendation log : recommendation)
			out += ","+log.toString();
		
		return out;
		
	}
	
    public NodeTerminal(int ID, List<Recommendation> recommendation){
    	
    	this.ID = ID;
    	this.attribute = "Preference";
    
    	this.recommendation = recommendation;
    	
    }
	
	
	public List<Recommendation> getRecommnedation(){
		return recommendation;
	}
	
}