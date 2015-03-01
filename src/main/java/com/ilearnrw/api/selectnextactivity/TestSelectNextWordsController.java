package com.ilearnrw.api.selectnextactivity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ilearnrw.utils.LanguageCode;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.selectnextword.LevelParameters;

import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;

import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class TestSelectNextWordsController {

	public static void main(String [ ] args)
	{
		
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
		
	//	TestSelectNextWordsController aux = new TestSelectNextWordsController();
	   
		
		
		try {

		  for(LanguageCode language:new LanguageCode[]{LanguageCode.GR,LanguageCode.EN})
				
			for(int game_i=0;game_i<9;game_i++){//game
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Instructions_"+language.toString()+"_"+appIds.get(game_i)+".csv"), "UTF-8"));
				boolean first = true;
				for (int la_i=0;la_i<9;la_i++){//Language area
					//int index = 0;
					//while(true){
						
						GameLevel level = LevelFactory.createLevel(language, la_i, appIds.get(game_i));	

						if(level.allowedDifficulty(la_i, 0)){
						for(int i1 =0; i1<level.getAllLevels(la_i, 0).size();i1++){
							
							String line = level.getAllLevels(la_i, 0).get(i1);
							LevelParameters param = new LevelParameters(line);
							
							
							System.out.println(appIds.get(game_i)+","+line);
							if(first){
								writer.write(la_i+","+param.mode+","+ param.ttsType+","+ line+","+level.instructions(la_i, 0, param));
								first = false;
							}else{
								writer.write("\n"+la_i+","+param.mode+","+ param.ttsType+","+line+","+level.instructions(la_i, 0, param));
							}


//							System.out.println(appIds.get(i)+","+line);
						}
					}
						
						/*String level = aux.SelectNextActivityTestSelectWords(LanguageCode.EN.toString(),i,0, j,index).getLevel();
						if (level==""){
							break;
						}else{
							System.out.println(j+","+i+",0,"+index+","+level+"");
						}
						index++;
					}*/
				}
				writer.close();
			}

						
		} catch (Exception e) {
			e.printStackTrace();

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

		List<String> activityLevels = level.getAllLevels(languageArea, difficulty);
		
		if(index<activityLevels.size())
			return new LevelJSON(activityLevels.get(index));
		else
			return new LevelJSON(activityLevels.get(0));

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
