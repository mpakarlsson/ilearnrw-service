package com.ilearnrw.api.selectnextword;


/*
 * @author Hector P. Martinez
 *  
 * Decoder for parameters on a level
 * 
 */
public class LevelParameters {

	
	public int wordLevel;
	public TypeFiller fillerType = TypeFiller.CLUSTER;
	public int batchSize;
	public TypeBasic speed;
	public int mode;
	public int accuracy;
	public TtsType ttsType;
	
	public TypeAmount amountDistractors = TypeAmount.FEW;
	public TypeAmount amountTricky = TypeAmount.NONE;
	
	
	public LevelParameters(String level){
					
		
		
		
			String[] parameters = level.split("-");
			
			for(String parameter : parameters){
				if(parameter.startsWith("W")){
					wordLevel = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("F")){
					int value = Integer.parseInt(parameter.substring(1));
					if(value>=TypeFiller.values().length)
						value = 0;
					fillerType = TypeFiller.values()[value];
					
				}else if(parameter.startsWith("B")){
					batchSize = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("S")){
					int value = Integer.parseInt(parameter.substring(1));
					if(value>=TypeBasic.values().length)
						value = 0;
					speed = TypeBasic.values()[value];
					
				}else if(parameter.startsWith("A")){
					accuracy = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("T")){
					int value = Integer.parseInt(parameter.substring(1));
					if(value>=TtsType.values().length)
						value = 0;
					ttsType = TtsType.values()[value];
					
				}else if(parameter.startsWith("M")){
					mode = Integer.parseInt(parameter.substring(1));
					
				}else if(parameter.startsWith("D")){
					int value = Integer.parseInt(parameter.substring(1));
					if(value>=TypeAmount.values().length)
						value = 0;
					amountDistractors = TypeAmount.values()[value];
					
				}else if(parameter.startsWith("X")){
					int value = Integer.parseInt(parameter.substring(1));
					if(value>=TypeAmount.values().length)
						value = 0;
					amountTricky = TypeAmount.values()[value];
				}
				
			}
			
		}	
	
	public String toString(){
		String output = "";
		
		output +="W"+wordLevel;
		output+= "-F"+fillerType.ordinal();
		output+= "-B"+batchSize;
		output+= "-S"+speed.ordinal();
		output+= "-A"+accuracy;
		output+= "-T"+ttsType.ordinal();
		output+= "-M"+mode;
		output+= "-D"+amountDistractors.ordinal();
		output+= "-X"+amountTricky.ordinal();
		
		return output;
	}
	
}
