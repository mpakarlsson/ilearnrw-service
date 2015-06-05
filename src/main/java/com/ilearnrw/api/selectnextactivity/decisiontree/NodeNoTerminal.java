package com.ilearnrw.api.selectnextactivity.decisiontree;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NodeNoTerminal extends Node {
	private int attributePosition;
	private int attributeMemory;
	private int defaultPosition;

	public NodeNoTerminal(String line){
		
		this.ID = Integer.valueOf(line.split(",")[1]);
		this.attribute = line.split(",")[2];
		this.attributePosition = Integer.valueOf(line.split(",")[3]);
		this.attributeMemory = Integer.valueOf(line.split(",")[4]);
		this.defaultPosition = Integer.valueOf(line.split(",")[5]);
		
    	this.children = new ArrayList<Node>();
    	this.constraints = new ArrayList<Object>();
	

	}
	
    public NodeNoTerminal(int ID, String attribute,int attributePosition, int attributeMemory, int defaultPosition){
    	
    	this.ID = ID;
    	this.children = new ArrayList<Node>();
    	this.constraints = new ArrayList<Object>();

    	this.attribute = attribute;
    	
    	this.attributePosition = attributePosition;//size of memory; which log one shold check; 0 is the previous, 1 the one before previous, etc; -1 is the default
    	this.attributeMemory = attributeMemory;//how long we should backtrack from attributePosition: 0 compares to previous one (attributePosition+1), etc  -1 if not a list, i.e. absolute value (only severity, speed,  accuracy and absoluteAmountDistractors)
    	this.defaultPosition = defaultPosition;//default child node to choose
    }
    
    
    public Node next(List<GeneralisedLog> logs){
    	
    	if(logs.size()>attributePosition){
    		
    		GeneralisedLog object = logs.get(attributePosition);
    		 
			try {
				Field att  = object.getClass().getDeclaredField(attribute);
				if(this.attributeMemory>-1){

					ParameterizedType pt = (ParameterizedType)att.getGenericType();
					Type enumType = pt.getActualTypeArguments()[0];
					
					List<Enum> attList = (List<Enum>)att.get(object);

					for(int i=0;i<constraints.size();i++){						

						
						if(constraints.get(i)==attList.get(this.attributeMemory)){
	    				//if(enumType.getClass().cast(constraints.get(i))==enumType.getClass().cast(attList.get(this.attributeMemory))){
					
	    					return children.get(i);
	    				}			
	    			}
				}else{
				
					for(int i=0;i<constraints.size();i++){

	    				if(att.getType().cast(constraints.get(i))==att.getType().cast(att.get(object))){
					
	    					return children.get(i);
	    				}			
	    			}
				}
				
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
    		
    	}
    	
    	if(children.size()>defaultPosition)
    		return children.get(defaultPosition);
    	else
    		return null;
    }
            
 public String toString(){
    	
    	String out = "";
    	
    	out += "Node,"+this.ID+","+this.attribute+","+this.attributePosition+","+this.attributeMemory+","+this.defaultPosition;
    	for(int i=0;i<this.children.size();i++){
    		
    		out +="\nEdge,"+this.ID+","+children.get(i).ID+","+this.constraints.get(i);
    		out += "\n"+ children.get(i).toString();
    	}
    	    	
    	return out;
    	
    }
    
}
