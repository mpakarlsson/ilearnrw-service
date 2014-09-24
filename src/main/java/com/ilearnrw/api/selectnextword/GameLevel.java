package com.ilearnrw.api.selectnextword;

import java.util.List;
/**
 * @author Hector P. Martinez
 *
 * Interface to select word for each of the games
 * 
 */

public interface GameLevel {

	
	public List<GameElement> getWords(LevelParameters parameters,int languageArea, int difficulty);
	
	/* Level of difficulty of the words */
	public int[] wordLevels(int languageArea, int difficulty);
	
	/* How filler words can be selected */
	public FillerType[] fillerTypes(int languageArea, int difficulty);
	
	/* How many target words (fillers not counted)*/
	public int[] batchSizes(int languageArea, int difficulty);
	
	/* Speed levels */
	public int[] speedLevels(int languageArea, int difficulty);
	
	/* Other challenge levels, e.g. number of filler words per target word */
	public int[] accuracyLevels(int languageArea, int difficulty);
		
	/* Alternative uses of TTS */
	public TtsType[] TTSLevels(int languageArea, int difficulty);

	/* Mode e.g. syllables or graphemes */
	public int[] modeLevels(int languageArea, int difficulty);
	
	public boolean allowedDifficulty(int languageArea, int difficulty);

	
}
