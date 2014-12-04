package com.ilearnrw.api.selectnextactivity.decisiontree;

import java.util.List;

public abstract class Node {
	
	public int ID;
	public String attribute;

    protected List<Node> children;
    protected List<Object> constraints;
	
    public void addChild(Node child,Object constraint){
    	
    	children.add(child);
    	constraints.add(constraint);
    	
    }
    
    
   
    
    public boolean isTerminal(){
    	return (this instanceof NodeTerminal);
    }
    
    public static Node fromString(String line){
    	
    	if(line.startsWith("Terminal")){
    		return new NodeTerminal(line);
    		
    	}else{
    		return new NodeNoTerminal(line);
    	}
    	
    }
    
    public List<Node> getChildren(){
    
    	return children;

    }
    

}
