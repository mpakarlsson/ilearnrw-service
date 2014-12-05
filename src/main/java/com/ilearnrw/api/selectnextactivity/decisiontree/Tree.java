package com.ilearnrw.api.selectnextactivity.decisiontree;

import ilearnrw.resource.ResourceLoader;
import ilearnrw.resource.ResourceLoader.Type;
import ilearnrw.user.profile.UserProfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.selectnextactivity.decisiontree.GeneralisedLog.GDifficulty;
import com.ilearnrw.api.selectnextactivity.decisiontree.GeneralisedLog.GSeverity;

public class Tree {
    private Node root;
        
    public static void main(String [ ] args)
    {
    	
    	//rebootTree("trees/sample.tree");
    	
    	Tree tree = new Tree("wrong/sample.tree");
//    	Tree tree = new Tree("trees/sample.tree");
    	
    	
    	System.out.println(tree.toString());

    	
    	
    	/*

    	
    	List<GeneralisedLog> logs = new ArrayList<GeneralisedLog>();
    	
    	logs.add(new GeneralisedLog("difficulty:NEXT_CLUSTER;severity:MASTER"));
    	logs.add(new GeneralisedLog("difficulty:SAME;severity:MASTER"));
    	logs.add(new GeneralisedLog("difficulty:NEXT_CLUSTER;severity:MASTER"));
    	logs.add(new GeneralisedLog("difficulty:NEXT_CLUSTER;severity:MASTER"));
    	
    	
    	
    	List<GeneralisedLog> recommendations = tree.getRecommendations(logs);
    	
    	
    	
    	System.out.println(recommendations.get(0).toString());
    	
    	*/
    	
    }
    
    
    
    public static void rebootTree(String filename){
    	Tree tree = rebootTree();
    	
     	Writer writer = null;

       	try {
       		
       		
       	    writer = new BufferedWriter(new OutputStreamWriter(
       	    		ResourceLoader.getInstance().getOutputStream(Type.DATA,filename), "utf-8"));
       	    writer.write(tree.toString());
       	} catch (IOException ex) {
       	  // report
       	} finally {
       	   try {writer.close();} catch (Exception ex) {}
       	}
       	
    	
    	
    	
    }
    
    public static Tree rebootTree(){

    	
        int numberNodes = 0;
   		NodeNoTerminal node = new NodeNoTerminal(numberNodes++,"severity",0,-1,0);//attributeMemory == 0 

       	Tree tree = new Tree(node);
       	    		
       	for(GSeverity valueTerminal : GSeverity.values()){
       			
       			switch(valueTerminal){
       				
       			
   					case NEW: //Same difficulty as before, different game, more difficult
   						
   						NodeNoTerminal next = new NodeNoTerminal(numberNodes++, "difficulty",0, 0,1);//difference between previous and previous difficulty
   		    			node.addChild(next, valueTerminal);

   		    			next.addChild(new NodeTerminal(numberNodes++,new ArrayList<Recommendation>(){{add(new Recommendation("difficulty:SAME_CLUSTER:IGNORE;gameType:DIFFERENT:IGNORE;challenge:HIGHER:IGNORE")); }}), GDifficulty.SAME);
   		    			next.addChild(new NodeTerminal(numberNodes++,new ArrayList<Recommendation>(){{add(new Recommendation("difficulty:SAME:IGNORE;gameType:DIFFERENT:IGNORE;challenge:HIGHER:IGNORE")); }}), GDifficulty.SAME_CLUSTER);
   		    			
   						break;
   					
   					case NEED_WORK: 
   						
   					
   						node.addChild(new NodeTerminal(numberNodes++,new ArrayList<Recommendation>(){{add(new Recommendation("difficulty:SAME_CLUSTER:IGNORE;gameType:SAME:IGNORE;challenge:HIGHER:IGNORE")); }}), valueTerminal);
   						break;									
   					
   					case REINFORCE:
   						
      					node.addChild(new NodeTerminal(numberNodes++,new ArrayList<Recommendation>(){{add(new Recommendation("difficulty:SAME_CLUSTER:IGNORE;gameType:DIFFERENT:IGNORE;challenge:HIGHER:IGNORE")); }}), valueTerminal);
   						break;	
   					
   					
   					case MASTER: 
   						
      					node.addChild(new NodeTerminal(numberNodes++,new ArrayList<Recommendation>(){{add(new Recommendation("difficulty:NEXT_CLUSTER:IGNORE;gameType:DIFFERENT:IGNORE;challenge:HIGHER:IGNORE")); }}), valueTerminal);

   						break;	
       			
       			}

       		    		    		
       	}
       	
       	
       	return tree;
       	
      
       	
    	
    	
    }
    
    public Tree(Node r) {
        root = r;
    }
    
    
    
    public String toString(){
    	    	
    	return root.toString();
    	
    }
    
    public Tree(String filename){
    	
    	List<String> lines = new ArrayList<String>();
		
		try {
			
			
			InputStream inS = ResourceLoader.getInstance().getInputStream(Type.DATA,filename);
			
			if(inS==null){
				for(String aux :  rebootTree().toString().split("\n"))
					lines.add(aux);
				
			}else{
				InputStreamReader in = new InputStreamReader(inS, "UTF-8");

				BufferedReader buf = new BufferedReader(in);
				String line = null;
				while((line=buf.readLine())!=null) {
					lines.add(line);
				}
				buf.close();
			}
						
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			for(String aux :  rebootTree().toString().split("\n"))
				lines.add(aux);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			for(String aux :  rebootTree().toString().split("\n"))
				lines.add(aux);
		} catch (IOException e) {
			e.printStackTrace();
			for(String aux :  rebootTree().toString().split("\n"))
				lines.add(aux);
		} 
		

    	HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
    	
    	
    	for(String line : lines){
  		
    		if(!line.startsWith("Edge")){
        	//	System.out.println(line);
    			Node next = Node.fromString(line);
    			nodes.put(next.ID, next);
    			System.out.println(line+" "+next.ID+" "+next.attribute);
    		}
    	}
    		
    	this.root = nodes.get(0);
    	
    	for(String line : lines){
    		
    		if(line.startsWith("Edge")){
    			
    			Node parent = nodes.get(Integer.valueOf(line.split(",")[1]));
    			Node child = nodes.get(Integer.valueOf(line.split(",")[2]));

    			String constraint = line.split(",")[3];

    			Field field;
				try {
					
					//System.out.println(parent.ID+" "+parent.attribute+" "+constraint);

					field =  GeneralisedLog.class.getDeclaredField(parent.attribute);
					
					if(field.getType().isEnum()){
						parent.addChild(child,  Enum.valueOf((Class<Enum>)field.getType(), constraint));
					}else if(field.getType().isPrimitive()){//assume int
						parent.addChild(child, Integer.valueOf(constraint));
					}else{
						
						ParameterizedType pt = (ParameterizedType)field.getGenericType();
						java.lang.reflect.Type enumType = pt.getActualTypeArguments()[0];
						parent.addChild(child,  Enum.valueOf((Class<Enum>)enumType, constraint));

						//System.out.println(parent.attribute+"->"+constraint+" -> "+enumType.getClass().getName());

						
						
						//parent.addChild(child, enumType.getClass().cast(constraint));
						//parent.addChild(child,  field.getType().cast( constraint));
					}
					

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} 
    			 
		
    					
    		/*	*/
    		
    		}
    	}
    	
    }
    
    
    public List<Recommendation> getRecommendations(UserProfile profile,List<LogEntry> lastLogs){
    	
    	
    	List<GeneralisedLog> constraints = GeneralisedLog.LogsToGeneralisedLogs(profile, lastLogs);    	
    	
    	Node nextNode = root;
    	
    	while(!nextNode.isTerminal()){
    		
    		nextNode = ((NodeNoTerminal)nextNode).next(constraints);
    		if(nextNode==null)
    			return new ArrayList<Recommendation>();
    		
    	}
    	return ((NodeTerminal)nextNode).getRecommnedation();
    	
    }
    
    
    
}
