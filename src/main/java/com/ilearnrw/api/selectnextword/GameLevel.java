package com.ilearnrw.api.selectnextword;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Hector P. Martinez
 *
 * Interface to select word for each of the games
 * 
 */

public abstract class GameLevel {
	
	public abstract List<GameElement> getWords(LevelParameters parameters,int languageArea, int difficulty);
		
	/* Level of difficulty of the words */
	public int[] wordLevels(int languageArea, int difficulty){
		return new int[]{0,1,2,3,4,5,6,7,8,9};
	}

	
	/* How many target words (fillers not counted)*/
	public int[] batchSizes(int languageArea, int difficulty){
		return new int[]{4,12,20};
	}
	
	/* Speed levels */
	public TypeBasic[] speedLevels(int languageArea, int difficulty){
		return TypeBasic.values();
	}
	
	/* Other challenge levels, e.g. number of filler words per target word */
	public int[] accuracyLevels(int languageArea, int difficulty){
		//Bike shed: number of words per door
		//Music hall: number of alternatives
		return new int[]{0};
	}
		
	/* Alternative uses of TTS */
	public TtsType[] TTSLevels(int languageArea, int difficulty){
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}
	
	
	/* How filler words can be selected */
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){
		return new TypeFiller[]{TypeFiller.CLUSTER};

	}
	
	/* Amount of distractors */
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		return TypeAmount.values();
	}
	
	/* Amount of tricky words */
	public TypeAmount[] amountTricky(int languageArea, int difficulty){
		return TypeAmount.values();
	}

	/* Mode e.g. syllables or graphemes */
	public int[] modeLevels(int languageArea, int difficulty){
		return new int[]{0};
	}
	
	/* Instructions for the games */
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		return "Not available";
	}
	
	
	public List<String> getAllLevels(int languageArea, int difficulty){
		List<String> output = new ArrayList<String>();
		
		if(allowedDifficulty(languageArea, difficulty)){
			for(int mode : modeLevels(languageArea, difficulty)){

				for(int accuracy : accuracyLevels(languageArea, difficulty)){

					for(TypeBasic speed : speedLevels(languageArea, difficulty)){

						for(int batchSize : batchSizes(languageArea, difficulty)){

							for(int wordLevels : wordLevels(languageArea, difficulty)){

								for (TypeFiller fillerTypes : fillerTypes(languageArea, difficulty)){

									for(TtsType ttstype : TTSLevels(languageArea, difficulty)){

										for(TypeAmount distractors: amountDistractors(languageArea, difficulty)){

											for(TypeAmount tricky: amountTricky(languageArea, difficulty)){
						
										
												String activityLevel = 	
														"M"+mode+"-"
												+ 	"A"+accuracy+"-"
												+ 	"S"+speed.ordinal()+"-"
												+ 	"B"+batchSize+"-"
												+ 	"W"+wordLevels+"-"
												+ 	"F"+fillerTypes.ordinal()+"-"
												+	"T"+ttstype.ordinal()+"-"
												+	"X"+tricky.ordinal()+"-"
												+	"D"+distractors.ordinal();
												
												output.add(activityLevel);

											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		return output;
	}
	
	public float challengeApproximation(int languageArea, int difficulty,LevelParameters target){
		int numberOptions = 0;
		int givenOption = 0;
		
		if(allowedDifficulty(languageArea, difficulty)){

			for(TtsType ttstype : TTSLevels(languageArea, difficulty)){

			for(int mode : modeLevels(languageArea, difficulty)){

				for(int accuracy : accuracyLevels(languageArea, difficulty)){

					for(TypeBasic speed : speedLevels(languageArea, difficulty)){
						
						if((target.ttsType==ttstype)&&(target.mode==mode)&&(target.accuracy==accuracy)&&(target.speed==speed))
							givenOption = numberOptions;
								
						numberOptions++;

	

					}
				}
			}
		}
		}
		
		
		float output = (float)givenOption/numberOptions;
		
		return output;
	}
	
	
	
	public abstract boolean allowedDifficulty(int languageArea, int difficulty);

	
}
