package com.ilearnrw.app.screening;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.profile.clusters.ProblemDescriptionCoordinates;
import ilearnrw.user.profile.clusters.ProfileClusters;
import ilearnrw.utils.LanguageCode;
import ilearnrw.utils.screening.ScreeningTest;
import ilearnrw.utils.screening.TestQuestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.services.LogEntryService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class ScreeningCreationControler {
	@Autowired
	private UserService userService;

	@Autowired
	LogEntryService logEntryService;

	@Autowired
	private TeacherStudentService teacherStudentService;
	
	// find css style at src/main/webapp/resources/css/style.css
	
	ArrayList<LogEntry> theLogs;

	@RequestMapping(value = "/screening", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('PERMISSION_ADMIN', 'PERMISSION_EXPERT', 'PERMISSION_TEACHER')")
	public String viewScreeningTestCreatorPage(@RequestParam(value = "cluster", required = false) Integer cluster, 
			@RequestParam(value = "fname", required = false) String fname,
			ModelMap model, HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		int userId = (Integer)request.getSession().getAttribute("userid");
		User user = userService.getUser(userId);
		int currentCluster = -1;
		if (cluster != null){
			currentCluster = cluster;
			System.err.println(cluster);
		}

		ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(user.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
		ProfileClusters pc = new ProfileClusters(pdi);
		
		ScreeningTest st = new ScreeningTest(pc);
		
		/*st.addQuestion("hi, how are you?", new ArrayList<String>(
			    Arrays.asList("fine", "thanks")), 1);
		st.addQuestion("what is your name?", new ArrayList<String>(
			    Arrays.asList("old", "years")), 1);
		st.addQuestion("second cluster, first question", new ArrayList<String>(
			    Arrays.asList("yeah", "baby")), 3);
		st.addQuestion("third cluster here", new ArrayList<String>(
			    Arrays.asList("go", "third")), 4);
		
		st.storeTest("data/EN_testing_screening.json");*/
		
		ScreeningTestList stl = new ScreeningTestList();
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"_test_list.json");
		
		if (stl.getFilenames().contains(fname))
			st.loadTest(ScreeningResources.path+fname+".json");
		
		model.put("showAll", st.getClusterQuestions(currentCluster) == null);
		model.put("cluster", currentCluster);
		if (currentCluster != -1){
			ArrayList<TestQuestion> tq = st.getClusterQuestions(cluster);
			String g = new Gson().toJson(tq);
			model.put("clustersQuestions", g);
			ArrayList<String> t = getClusterWords(user.getLanguage().equalsIgnoreCase("EN")?LanguageCode.EN:LanguageCode.GR, 
							currentCluster);
			 g = new Gson().toJson(t);
			 model.put("clusterWords", g);
		}
		else {
			model.put("clustersQuestions", null);
		}
		model.put("profileClusters", pc);
		String g = new Gson().toJson(stl);
		model.put("screeningTestList", g);
		model.put("fname", fname);

		model.put("screeningTest", st);
		return "screening/creator";
	}

	@RequestMapping(value = "/testviewer", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('PERMISSION_ADMIN', 'PERMISSION_EXPERT', 'PERMISSION_TEACHER')")
	public String viewScreeningTest(ModelMap model, HttpServletRequest request, 
			@RequestParam(value = "fname", required = true) String fname) 
			throws ProfileProviderException, Exception {
		int userId = (Integer)request.getSession().getAttribute("userid");
		User user = userService.getUser(userId);
		ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(user.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
		ProfileClusters pc = new ProfileClusters(pdi);
		ScreeningTest st = new ScreeningTest();

		ScreeningTestList stl = new ScreeningTestList();
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"_test_list.json");
		
		if (stl.getFilenames().contains(fname))
			st.loadTest(ScreeningResources.path+fname+".json");

		model.put("fname", fname);
		model.put("profileClusters", pc);
		model.put("screeningTest", st);
		return "screening/test_viewer";
	}

	@RequestMapping(value = "/{userId}/screeningtest", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('PERMISSION_ADMIN', 'PERMISSION_EXPERT', 'PERMISSION_TEACHER')")
	public String takeScreeningTest(@PathVariable Integer userId, 
			ModelMap model, HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		int teacherId = (Integer)request.getSession().getAttribute("userid");
		User teacher = userService.getUser(teacherId);
		User student = userService.getUser(userId);

		ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(teacher.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
		ProfileClusters pc = new ProfileClusters(pdi);
		ScreeningTest st = new ScreeningTest();
		
		if (teacherStudentService.getStudentList(teacher).contains(student)){
			ScreeningTestList stl = new ScreeningTestList();
			stl.loadScreeningTestList(ScreeningResources.path+teacher.getLanguage()+"_test_list.json");
			st.loadTest(ScreeningResources.path+stl.getDefaultTest()+".json");
			model.put("username", student.getUsername());
			model.put("userId", userId);
			model.put("profileClusters", pc);
			model.put("screeningTest", st);
		}
		return "screening/test_page";
	}

	@RequestMapping(headers = { "Accept=application/json" }, value = "/updatecluster", method = RequestMethod.POST)
	public @ResponseBody
	int updateClusterQuestions(@Valid @RequestBody TestQuestion question,
			@RequestParam(value = "cluster", required = true) Integer cluster, 
			@RequestParam(value = "fname", required = true) String fname, 
			@RequestParam(value = "action", required = true) String action, 
			@RequestParam(value = "id", required = false) Integer id, 
			HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		int teacherId = (Integer)request.getSession().getAttribute("userid");
		User user = userService.getUser(teacherId);
		ScreeningTest st = new ScreeningTest();
		ScreeningTestList stl = new ScreeningTestList();
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"_test_list.json");
		if (stl.getFilenames().contains(fname)){
			st.loadTest(ScreeningResources.path+fname+".json");
			if (action.equalsIgnoreCase("add")){
				int respId = st.addQuestion(question.getQuestion(), question.getRelatedWords(), cluster);
				st.storeTest(ScreeningResources.path+fname+".json");
				return respId;
			}
			else if (action.equalsIgnoreCase("delete")){
				st.deleteQuestion(cluster, id);
				st.storeTest(ScreeningResources.path+fname+".json");
				return id;
			}
			else if (action.equalsIgnoreCase("update")){
				st.editQuestion(cluster, id, question.getQuestion(), question.getRelatedWords());
				st.storeTest(ScreeningResources.path+fname+".json");
				return id;
			}
		}
		return -1;
	}

	@RequestMapping(value = "/updatetests", method = RequestMethod.POST)
	public @ResponseBody
	ScreeningTestList updateTests(@RequestParam(value = "action", required = true) String action, 
			@RequestParam(value = "testName", required = true) String testName, 
			HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		int teacherId = (Integer)request.getSession().getAttribute("userid");
		User user = userService.getUser(teacherId);
		ScreeningTestList stl = new ScreeningTestList();
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"_test_list.json");
		if (action.equalsIgnoreCase("addTest")){
			stl.addNewTest(testName);
			ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(user.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
			ProfileClusters pc = new ProfileClusters(pdi);
			ScreeningTest st = new ScreeningTest(pc);
			st.storeTest(testName+".json");
		}
		else if (action.equalsIgnoreCase("deleteTest")){
			stl.deleteTest(testName);
		}
		else if (action.equalsIgnoreCase("defaultTest")){
			stl.setDefaultTest(testName);
		}
		stl.storeScreeningTestList(ScreeningResources.path+user.getLanguage()+"_test_list.json");
		return stl;
	}

	@RequestMapping(headers = { "Accept=application/json" }, value = "/setupstudent", method = RequestMethod.POST)
	public @ResponseBody
	int setupStudent(@Valid @RequestBody ArrayList<LogEntry> logs,
			@RequestParam(value = "userId", required = true) Integer userId, 
			HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		java.util.Date date= new java.util.Date();
		User user = userService.getUser(userId);
		int teacherId = (Integer)request.getSession().getAttribute("userid");
		if (logs != null){
			for (LogEntry t: logs)
				logEntryService.insertData(t);
			//dataloggerController.addLogs(logs);
		}
		//LogEntry le = new LogEntry(user.getUsername(), "PROFILE_SETUP", new Timestamp(date.getTime()), 
		//		"USER_SEVERITIES_SET",  "", -1, -1, 
		//		0.0, "", "EVALUATION_MODE", "");
		return userId;
	}
	
	private ArrayList<String> getClusterWords(LanguageCode lc, int cluster){
		ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(lc);
		ProfileClusters pc = new ProfileClusters(pdi);
		ArrayList<ProblemDescriptionCoordinates> prbs = pc.getClusterProblems(cluster);
		ProblemWordListLoader pwll;
		EasyHardList thelist;
		ArrayList<String> res = new ArrayList<String>();
		for (ProblemDescriptionCoordinates pr : prbs){
			pwll = new ProblemWordListLoader(lc, pr.getCategory(), pr.getIndex());
			thelist = new EasyHardList(pwll.getItems());
			for (String x : thelist.getEasy()){
				   if (!res.contains(x))
				      res.add(x);
				}
		}
		return res;
	}
	//page that displays the list of problem categories
	//@RequestMapping(value = "/users/{userId}/initprofile", method = RequestMethod.GET)
	/*@RequestMapping(value = "/users/{userId}/initprofile", method = RequestMethod.GET)
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
		
		LogEntryResult ler = dataloggerController.getLogs(current.getUsername(), null, null, null, 
				"USER_SEVERITIES_SET", "PROFILE_SETUP");
		
		ArrayList<Integer> categoriesSet = new ArrayList<Integer>();
		if (ler != null)
			for (LogEntry le : ler.getResults())
				categoriesSet.add(le.getProblemCategory());

		model.put("categoriesSet", categoriesSet);
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
		int index = (start+end)/2;
		
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
		handleLogs();
		
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
			
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null){
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		createSucceedFailedLogs(current.getUsername(), category, index, ""+difficulty, wordlist, succeedlist);
		handleLogs();

		String nextPage = "initialize";
		int p[] = getNextIndices(start, end ,wordlist, succeedlist);
		if ((int)start >= (int)end-1 && (int)difficulty == 0){
			int all = profile.getUserProblems().getRowLength(category);
			for (int i=0;i<start;i++)
				profile.getUserProblems().setUserSeverity(category, i, 2);
			for (int i=start;i<all;i++)
				profile.getUserProblems().setUserSeverity(category, i, 3);
			profileProvider.updateProfile(userId, profile);
			difficulty = 1;
			start = 0;
			p[0] = start;
			p[1] = end;
		}
		if ((int)start >= (int)end-1 && (int)difficulty == 1){
			for (int i=0;i<start;i++)
				profile.getUserProblems().setUserSeverity(category, i, 1);
			profileProvider.updateProfile(userId, profile);
			nextPage = "initprofile";
			createCategoryDoneLogs(current.getUsername(), category);
			handleLogs();
		}
		model.put("userId", userId);
		model.put("nextPage", nextPage);
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
	
	private void createCategoryDoneLogs(String username, int problemCategory){
		if (theLogs == null)
			theLogs = new ArrayList<LogEntry>();
		java.util.Date date= new java.util.Date();
		LogEntry le = new LogEntry(username, "PROFILE_SETUP", new Timestamp(date.getTime()), 
				"USER_SEVERITIES_SET",  "", problemCategory, -1, 
				0, "", "EVALUATION_MODE", "");
		theLogs.add(le);
	}
	
	private void handleLogs(){
		if (theLogs != null && !theLogs.isEmpty()){
			if (!theLogs.isEmpty()){
				try {
					//LogEntry le = theLogs.remove(0);
					dataloggerController.addLogs(theLogs);
					theLogs.clear();
					//logEntryService.insertData(le);
					//cubeService.handle(le);
				} catch (Exception ex) {
					LOG.debug("Error when received log: " + ex.getMessage());
				}
			}
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
			if (e <= s+1){
				res[0] = e;
				res[1] = e;
			}
			else{
				res[0] = (s+e+1)/2;
				res[1] = e;
			}
		}
		else {
			if (e <= s+1){
				res[0] = s;
				res[1] = s;
			}
			else{
				res[0] = s;
				res[1] = (s+e-1)/2;
			}
		}
		return res;
	}*/
}
