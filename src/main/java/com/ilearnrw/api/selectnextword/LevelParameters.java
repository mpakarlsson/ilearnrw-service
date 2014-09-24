package com.ilearnrw.api.selectnextword;


/*
 * @author Hector P. Martinez
 *  
 * Decoder for parameters on a level
 * 
 */
public class LevelParameters {

	
	public int wordLevel;
	public FillerType fillerType;
	public int batchSize;
	public int speed;
	public int mode;
	public int accuracy;
	public TtsType ttsType;
	
	public LevelParameters(String level){
					
			String[] parameters = level.split("-");
			
			for(String parameter : parameters){
				if(parameter.startsWith("W")){
					wordLevel = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("F")){
					fillerType = FillerType.values()[Integer.parseInt(parameter.substring(1))];
					
				}else if(parameter.startsWith("B")){
					batchSize = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("S")){
					speed = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("A")){
					accuracy = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("T")){
					ttsType = TtsType.values()[Integer.parseInt(parameter.substring(1))];
				}else if(parameter.startsWith("M")){
					mode = Integer.parseInt(parameter.substring(1));
				}
				
			}
			
		}		
	
}
