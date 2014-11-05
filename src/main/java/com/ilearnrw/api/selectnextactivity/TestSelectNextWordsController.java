package com.ilearnrw.api.selectnextactivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.info.dao.InfoDaoImpl;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.app.games.mapping.GamesInformation;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class TestSelectNextWordsController {

	public static void main(String [ ] args)
	{
		
		TestSelectNextWordsController aux = new TestSelectNextWordsController();
	   
		for (int j=0;j<9;j++){
		for(int i=0;i<9;i++){
			int index = 0;
			while(true){
				String level = aux.SelectNextActivityTestSelectWords(LanguageCode.GR.toString(),i,0, j,index).getLevel();
				if (level==""){
					break;
				}else{
					System.out.println(j+","+i+",0,"+index+","+level);
				}
				index++;
			}
		}
		}
		
	}
	
	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	UserService userService;

	@Autowired
	CubeService cubeService;


	@RequestMapping(value = "/activity/next_test", method = RequestMethod.GET)
	public @ResponseBody
	LevelJSON SelectNextActivityTestSelectWords(
			@RequestParam(value = "language", required = true) String language,
			@RequestParam(value = "languageArea", required = true) int languageArea,
			@RequestParam(value = "difficulty", required = true) int difficulty,
			@RequestParam(value = "appID", required = true) int appID,
			@RequestParam(value = "index", required = true) int index)	 {





		//	if(GamesInformation.getAppRelatedProblems(appID, LanguageCode.fromString(language)).contains(languageArea)){
		ArrayList<String> appIds = new ArrayList<String>();
		appIds.add("MAIL_SORTER");
		appIds.add("WHAK_A_MOLE");
		appIds.add("ENDLESS_RUNNER");
		appIds.add("HARVEST");
		appIds.add("SERENADE_HERO");
		appIds.add("MOVING_PATHWAYS");
		appIds.add("EYE_EXAM");
		appIds.add("TRAIN_DISPATCHER");
		appIds.add("DROP_CHOPS");

		GameLevel level = LevelFactory.createLevel(LanguageCode.fromString(language), languageArea, appIds.get(appID));	

		int i=0;

		String activityLevel = "";

		if(level.allowedDifficulty(languageArea, difficulty)){
			for(int mode : level.modeLevels(languageArea, difficulty)){

				for(int accuracy : level.accuracyLevels(languageArea, difficulty)){

					for(int speed : level.speedLevels(languageArea, difficulty)){

						for(int batchSize : level.batchSizes(languageArea, difficulty)){

							for(int wordLevels : level.wordLevels(languageArea, difficulty)){

								for (FillerType fillerTypes : level.fillerTypes(languageArea, difficulty)){

									for(TtsType ttstype : level.TTSLevels(languageArea, difficulty)){

										activityLevel = 	"M"+mode+"-"
												+ 	"A"+accuracy+"-"
												+ 	"S"+speed+"-"
												+ 	"B"+batchSize+"-"
												+ 	"W"+wordLevels+"-"
												+ 	"F"+fillerTypes.ordinal()+"-"
												+	"T"+ttstype.ordinal();


										if (i==index){

											return new LevelJSON(activityLevel);
										}else{
											i++;
										}


									}



								}
							}
						}


					}
				}

			}

			//return new LevelJSON(activityLevel);


		//}else{


//			return new LevelJSON("");
		}

		return new LevelJSON("");

	}


	class LevelJSON  implements Serializable {
		private static final long serialVersionUID = 1L;
		private String level;

		public LevelJSON(String l){

			level = l;
		}

		public String getLevel(){
			return level;
		}

		public void setLevel(String l){
			level = l;
		}

	}

}
