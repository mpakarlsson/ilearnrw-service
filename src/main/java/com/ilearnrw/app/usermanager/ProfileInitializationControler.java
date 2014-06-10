package com.ilearnrw.app.usermanager;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ilearnrw.api.datalogger.DataloggerController;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.datalogger.services.LogEntryService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class ProfileInitializationControler {
	private static Logger LOG = Logger.getLogger(DataloggerController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	IProfileProvider profileProvider;
	
	@Autowired
	DataloggerController dataloggerController;
	// find css style at src/main/webapp/resources/css/style.css
	
	ArrayList<LogEntry> theLogs;

	//page that displays the list of problem categories
	@RequestMapping(value = "/users/{userId}/initprofile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfileInitializationPage(@PathVariable int userId, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("profile", profile);
				
		return "users/profile.startpage";
	}
	
	//page that displays the words of a problem category
	@RequestMapping(value = "/users/{userId}/initialize", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfileInitializationForm(@PathVariable int userId, 
			@RequestParam(value="category", required = true) Integer category, 
			@RequestParam(value="start", required = true) Integer start, 
			@RequestParam(value="difficulty", required = true) Integer difficulty, 
			@RequestParam(value="end", required = true) Integer end, ModelMap model) 
			throws ProfileProviderException, Exception {
		handleLogs();
		UserProfile profile = null;
		User current = null;
		if (category == null || start == null || end == null){
			category = -1;
			start = -1;
			end = -1;
		}
		if (difficulty>=1)
			difficulty = 1;
		else 
			difficulty = 0;
		
		int index = (start+end)/2;
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}
		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		List<GameElement> result = new ArrayList<GameElement>();
		LanguageCode lan = profile.getLanguage();
		ProblemWordListLoader pwll = new ProblemWordListLoader(lan, category, index);
		EasyHardList thelist = new EasyHardList(pwll.getSentenceList());

		ArrayList<String> ws = thelist.getRandom(7, difficulty);
		for (String w : ws){
			if (lan == LanguageCode.EN)
				result.add(new GameElement(false, new EnglishWord(w), category, index));
			else
				result.add(new GameElement(false, new GreekWord(w), category, index));
		}
		
		createDisplayedLogs(current.getUsername(), category, index, ""+difficulty, ws);
		
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("category", category);
		model.put("difficulty", difficulty);
		model.put("index", index);
		model.put("start", start);
		model.put("end", end);
		model.put("profile", profile);
		model.put("result", result);
				
		return "users/profile.initialize";
	}
	
	@RequestMapping(value = "/users/{userId}/confirmanswers", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewConfirmAnswersPage(@PathVariable int userId, 
			@RequestParam(value="difficulty", required = true) Integer difficulty, 
			@RequestParam(value="category", required = true) Integer category, 
			@RequestParam(value="index", required = true) Integer index,
			@RequestParam(value="start", required = true) Integer start, 
			@RequestParam(value="end", required = true) Integer end, 
			@RequestParam(value="succeed", required = false) String[] succeed, 
			@RequestParam(value="words", required = true) String[] words, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		ArrayList<String> wordlist = convertArrayToUTF8(words);
		ArrayList<String> succeedlist = convertArrayToUTF8(succeed);
			
		int p[] = getNextIndices(start, end ,wordlist, succeedlist);
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		createSucceedFailedLogs(current.getUsername(), category, index, ""+difficulty, wordlist, succeedlist);
				
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("difficulty", difficulty);
		model.put("category", category);
		model.put("index", (p[0]+p[1])/2);
		model.put("start", p[0]);
		model.put("end", p[1]);
		model.put("succeedlist", succeedlist);
		model.put("wordlist", wordlist);
		model.put("profile", profile);
				
		return "users/profile.confirmpage";
	}
	
	private void createDisplayedLogs(String username, int problemCategory, int problemIndex, 
			String level, ArrayList<String> words){
		if (theLogs == null)
			theLogs = new ArrayList<LogEntry>();
		java.util.Date date= new java.util.Date();
		for (String s : words){
			LogEntry le = new LogEntry(username, "PROFILE_SETUP", new Timestamp(date.getTime()), 
					"WORD_DISPLAYED",  s, problemCategory, problemIndex, 
					0, level, "EVALUATION_MODE", "");
			theLogs.add(le);
		}
	}
	
	private void createSucceedFailedLogs(String username, int problemCategory, int problemIndex, 
			String level, ArrayList<String> words, ArrayList<String> succeedwords){
		if (theLogs == null)
			theLogs = new ArrayList<LogEntry>();
		java.util.Date date= new java.util.Date();
		for (String s : words){
			if (succeedwords.contains(s)){
				LogEntry le = new LogEntry(username, "PROFILE_SETUP", new Timestamp(date.getTime()), 
						"WORD_SUCCESS",  s, problemCategory, problemIndex, 
						0, level, "EVALUATION_MODE", "");
				theLogs.add(le);
			}
			else{
				LogEntry le = new LogEntry(username, "PROFILE_SETUP", new Timestamp(date.getTime()), 
						"WORD_FAILED",  s, problemCategory, problemIndex, 
						0, level, "EVALUATION_MODE", "");
				theLogs.add(le);
			}
		}
	}
	
	private void handleLogs(){
		if (theLogs != null && !theLogs.isEmpty()){
			int x = theLogs.size();
			while (!theLogs.isEmpty()){
				System.err.println(theLogs.get(0).getApplicationId());
				try {
					LogEntry le = theLogs.remove(0);
					dataloggerController.addLog(le);
					//logEntryService.insertData(le);
					//cubeService.handle(le);
				} catch (Exception ex) {
					LOG.debug("Error when received log: " + ex.getMessage());
	
				}
			}
			System.err.println(x);
		}
		else if (theLogs == null)
			theLogs = new ArrayList<LogEntry>();
	}
	
	private ArrayList<String> convertArrayToUTF8(String str[]) throws UnsupportedEncodingException{
		if (str == null)
			return new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i<str.length; i++){
			byte[] in = str[i].getBytes("iso-8859-1");
			res.add(new String(in,"UTF-8"));
		}
		return res;
	}
	
	private int[] getNextIndices(int s, int e, ArrayList<String> words, ArrayList<String> succeed){
		int threshold = 5;
		int suc = 0;
		for (String w : words){
			if (succeed.contains(w))
				suc++;
		}
		int res[] = new int[2];
		if (suc >= threshold){
			res[0] = (s+e)/2 +1;
			res[1] = e;
		}
		else {
			res[0] = s;
			res[1] = (s+e)/2 -1;
		}
		return res;
	}
}
