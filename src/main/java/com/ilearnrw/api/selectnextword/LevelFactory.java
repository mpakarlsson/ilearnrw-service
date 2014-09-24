package com.ilearnrw.api.selectnextword;

import com.ilearnrw.api.selectnextword.levels.*;
import com.ilearnrw.app.games.mapping.GamesInformation;

import ilearnrw.utils.LanguageCode;

public class LevelFactory {

	public static GameLevel createLevel(LanguageCode lc, int languageArea, String appName){
		
		int appId = GamesInformation.getAppID(appName);
		
		if (appId==-1)
			appId = GamesInformation.getAppIDfromStringID(appName);
		
		if (appId==-1)
			return null;
		
		
		GameLevel level = null;
		
		if (lc == LanguageCode.GR){
			
			switch(appId){
			
			case 0://Mail Sorter//Mail room
				break;
			case 1://Whack a Mole
				break;
			case 2://Endless Runner
				break;
			case 3://Harvest
				break;
			case 4://Serenade Hero
				break;
			case 5://Moving Pathways
				break;
			case 6://Eye Exam
				break;
			case 7://Train Dispatcher
				break;
				
			case 8://Drop Chops
				break;
				
			}
		}else{
			
			switch(appId){
			
			case 0://Mail Sorter//Mail room
				return new MailRoomUK();
			case 1://Whack a Mole
				return new MonkeyHotelUK();
			case 2://Endless Runner
				return new BikeShedUK();
			case 3://Harvest
				return new GardenUK();
			case 4://Serenade Hero
				return new MusicHallUK();
			case 5://Moving Pathways
				return new CityHallUK();
			case 6://Eye Exam
				return new BridgeUK();
			case 7://Train Dispatcher
				return new TrainStationUK();
				
			case 8://Drop Chops
				return new JunkyardUK();
				
			}
			
		}
		
		return level;
		
		
	}
	
	
}
