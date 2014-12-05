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
		
		return createLevel(lc,languageArea,appId);
		
	}
	
	public static GameLevel createLevel(LanguageCode lc, int languageArea, int appID){

		
		GameLevel level = null;
		
		if (lc == LanguageCode.GR){
			
			switch(appID){
			
			case 0://Mail Sorter//Mail room
				return new MailRoomGR();
			case 1://Whack a Mole
				return new MonkeyHotelGR();
			case 2://Endless Runner
				return new BikeShedGR();
			case 3://Harvest
				return new GardenGR();
			case 4://Serenade Hero
				return new MusicHallGR();
			case 5://Moving Pathways
				return new CityHallGR();
			case 6://Eye Exam
				return new BridgeGR();
			case 7://Train Dispatcher
				return new TrainStationGR();
			case 8://Drop Chops
				return new JunkyardGR();
				
			}
		}else{
			
			switch(appID){
			
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
