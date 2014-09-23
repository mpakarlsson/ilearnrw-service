package com.ilearnrw.app.screening;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import ilearnrw.languagetools.LanguageAnalyzerAPI;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.languagetools.greek.GreekDictionary;
import ilearnrw.languagetools.greek.GreekLanguageAnalyzer;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.clusters.ClusterInfo;
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
import com.ilearnrw.api.datalogger.DataloggerController;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.services.LogEntryService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
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
	private LogEntryService logEntryService;

	@Autowired
	private IProfileProvider profileProvider;

	//@Autowired
	//DataloggerController dataloggerController;

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
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"/test_list.json");
		
		if (stl.getFilenames().contains(fname))
			st.loadTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");
		
		model.put("showAll", st.getClusterQuestions(currentCluster) == null);
		model.put("cluster", currentCluster);
		model.put("profileClusters", pc);
		String g = new Gson().toJson(stl);
		model.put("screeningTestList", g);
		model.put("fname", fname);

		model.put("screeningTest", st);
		if (currentCluster != -1){
			ArrayList<TestQuestion> tq = st.getClusterQuestions(cluster);
			g = new Gson().toJson(tq);
			model.put("clustersQuestions", g);
			ArrayList<WordsInsideCategory> full;
			if (pdi.getLanguage() == LanguageCode.GR)
				full = getGreekClusterWords(currentCluster);
			else
				full = getEnglishClusterWords(currentCluster);
			ArrayList<String> t = new ArrayList<String>();
			for (WordsInsideCategory list : full){
				for (String ws : list.getWords())
					if (!t.contains(ws))
						t.add(ws);
			}
			g = new Gson().toJson(t);
			model.put("clusterWords", g);
			g = new Gson().toJson(full);
			model.put("wordsInsideCategory", g);
			model.put("clusterCategories", getClusterRelatedCategoriesAsString(pdi, cluster));
			g = new Gson().toJson(getActiveQuestions(st));
			model.put("activeQuestions", g);
			g = new Gson().toJson(pc.getClusterProblems(cluster));
			model.put("clusterDescriptions", g);
		}
		else {
			model.put("clustersQuestions", null);
			HashMap<Integer, String> clustersCategoriesMap = new HashMap<Integer, String>();
			for (Integer cn : pc.getClustersNumbers()){
				clustersCategoriesMap.put(cn, getClusterRelatedCategoriesAsString(pdi, cn));
			}
			model.put("clustersCategoriesMap", clustersCategoriesMap);
		}
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
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"/test_list.json");
		
		if (stl.getFilenames().contains(fname))
			st.loadTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");

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
		
		//if (teacherStudentService.getStudentList(teacher).contains(student)){
			ScreeningTestList stl = new ScreeningTestList();
			stl.loadScreeningTestList(ScreeningResources.path+teacher.getLanguage()+"/test_list.json");
			st.loadTest(ScreeningResources.path+teacher.getLanguage()+"/"+stl.getDefaultTest()+".json");
			model.put("username", student.getUsername());
			model.put("userId", userId);
			model.put("profileClusters", pc);
			model.put("screeningTest", st);
		//}
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
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"/test_list.json");
		if (stl.getFilenames().contains(fname)){
			st.loadTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");
			if (action.equalsIgnoreCase("add")){
				int respId = st.addQuestion(question.getQuestion(), question.getRelatedWords(), 
						question.isAttachRelWords(), cluster);
				st.storeTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");
				return respId;
			}
			else if (action.equalsIgnoreCase("delete")){
				st.deleteQuestion(cluster, id);
				st.storeTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");
				return id;
			}
			else if (action.equalsIgnoreCase("update")){
				st.editQuestion(cluster, id, question.getQuestion(), question.getRelatedWords(), 
						question.isAttachRelWords());
				st.storeTest(ScreeningResources.path+user.getLanguage()+"/"+fname+".json");
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
		stl.loadScreeningTestList(ScreeningResources.path+user.getLanguage()+"/test_list.json");
		if (action.equalsIgnoreCase("addTest")){
			stl.addNewTest(testName);
			ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(user.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
			ProfileClusters pc = new ProfileClusters(pdi);
			ScreeningTest st = new ScreeningTest(pc);
			st.storeTest(ScreeningResources.path+user.getLanguage()+"/"+testName+".json");
		}
		else if (action.equalsIgnoreCase("deleteTest")){
			stl.deleteTest(testName);
		}
		else if (action.equalsIgnoreCase("defaultTest")){
			stl.setDefaultTest(testName);
		}
		stl.storeScreeningTestList(ScreeningResources.path+user.getLanguage()+"/test_list.json");
		return stl;
	}

	@RequestMapping(headers = { "Accept=application/json" }, value = "/{userId}/setupstudent", method = RequestMethod.POST)
	public @ResponseBody
	int setupStudent(@Valid @RequestBody ArrayList<LogEntry> logs,
			@PathVariable Integer userId, 
			HttpServletRequest request) 
			throws ProfileProviderException, Exception {
		java.util.Date date= new java.util.Date();
		User user = userService.getUser(userId);
		int supervisorId = (Integer)request.getSession().getAttribute("userid");
		User supervisor = userService.getUser(supervisorId);
		
		ProblemDefinitionIndex pdi = new ProblemDefinitionIndex(user.getLanguage().equals("EN")?LanguageCode.EN:LanguageCode.GR);
		ProfileClusters pc = new ProfileClusters(pdi);
		ArrayList<ClusterStats> stats = new ArrayList<ClusterStats>();
		
		for (int cl : pc.getClustersNumbers()){
			stats.add(new ClusterStats(cl));
		}
		
		if (logs != null){
			for (LogEntry t: logs){
				logEntryService.insertData(t);
				//cluster number is : t.getValue();

				ClusterStats cstats = getStatsObject(stats, Integer.parseInt(t.getValue()));
				if (cstats == null)
					continue;
					
				if (t.getTag().equals("WORD_SUCCESS")){
					cstats.highScore++;
					cstats.userScore++;
				}
				else if (t.getTag().equals("WORD_FAILED") || t.getTag().equals("WORD_NOT_ANSWERED")){
					cstats.highScore++;
				}
			}
			//dataloggerController.addLogs(logs);
			UserProfile profile = profileProvider.getProfile(userId);
			for (ClusterInfo ci : pc.getClusters()){
				ClusterStats cstats = getStatsObject(stats, ci.getClusterNumber());
				if (cstats == null)
					continue;
				
				int severity = 3;
				if (cstats.highScore>2 && cstats.getRate()==1)
					severity = 1;
				else if (cstats.highScore>2 && cstats.getRate()>0.5)
					severity = 2;
				System.err.println(ci.getClusterNumber()+"  --  "+cstats.highScore+" ,, "+cstats.userScore);
				if (cstats.highScore<2)
					continue;
				for (ProblemDescriptionCoordinates pdc : ci.getRelatedProblems()){
					if (profile.getUserProblems().getProblemDefinition(pdc.getCategory()).getSeverityType().equals("binary"))
						profile.getUserProblems().setUserSeverity(pdc.getCategory(), pdc.getIndex(), severity == 1?0:1);
					else 
						profile.getUserProblems().setUserSeverity(pdc.getCategory(), pdc.getIndex(), severity);
				}
				LogEntry le = new LogEntry(user.getUsername(), "PROFILE_SETUP", new Timestamp(date.getTime()), 
						"USER_SEVERITIES_SET",  "", -1, -1, 
						0, "cluster "+ci.getClusterNumber(), "EVALUATION_MODE", ""+severity, supervisor.getUsername());
				logEntryService.insertData(le);
			}
			profileProvider.updateProfile(userId, profile);
		}
		return userId;
	}
	
	private ArrayList<WordsInsideCategory> getEnglishClusterWords(int cluster){
		ProfileClusters pc = new ProfileClusters(new ProblemDefinitionIndex(LanguageCode.EN));
		LanguageAnalyzerAPI lan = EnglishLanguageAnalyzer.getInstance();
		ArrayList<ProblemDescriptionCoordinates> prbs = pc.getClusterProblems(cluster);
		ProblemWordListLoader pwll;
		EasyHardList thelist;
		ArrayList<WordsInsideCategory> res = new ArrayList<WordsInsideCategory>();
		int target = 40;
		for (ProblemDescriptionCoordinates pr : prbs){
			WordsInsideCategory wic = new WordsInsideCategory(pr.getCategory(), pr.getIndex());
			pwll = new ProblemWordListLoader(LanguageCode.EN, pr.getCategory(), pr.getIndex());
			thelist = new EasyHardList(pwll.getItems());
			boolean letsDoIt = true;
			int freq[] = {82911, 39051, 18441, 11710, 0};
			int i = 0;
			while (letsDoIt && i<freq.length){
				for (String x : thelist.getEasy()){
					if (res.contains(x) || x.contains("\'"))
						continue;
					lan.setWord(new EnglishWord(x));
					if(lan.getWord().getFrequency()>freq[i]){
						wic.addWord(x);
					}
					if (wic.getWords().size() >= target){
						letsDoIt = false;
						break;
					}
				}
				i++;
			}
			res.add(wic);
		}
		return res;
	}
	
	private ArrayList<WordsInsideCategory> getGreekClusterWords(int cluster){
		ProfileClusters pc = new ProfileClusters(new ProblemDefinitionIndex(LanguageCode.GR));
		ArrayList<ProblemDescriptionCoordinates> prbs = pc.getClusterProblems(cluster);
		ProblemWordListLoader pwll;
		EasyHardList thelist;
		int target = 40;
		ArrayList<WordsInsideCategory> res = new ArrayList<WordsInsideCategory>();
		for (ProblemDescriptionCoordinates pr : prbs){
			WordsInsideCategory wic = new WordsInsideCategory(pr.getCategory(), pr.getIndex());
			pwll = new ProblemWordListLoader(LanguageCode.GR, pr.getCategory(), pr.getIndex());
			thelist = new EasyHardList(pwll.getItems());
			for (String x : thelist.getEasy()){
				if (!res.contains(x) && !x.contains("\'")){
					wic.addWord(x);
				}
				if (wic.getWords().size() >= target){
					break;
				}
			}
			res.add(wic);
		}
		return res;
	}
	
	private String getClusterRelatedCategoriesAsString(ProblemDefinitionIndex pdi, int cluster){
		ProfileClusters pc = new ProfileClusters(pdi);
		ArrayList<Integer> visited = new ArrayList<Integer>();
		String result = "";
		for (ProblemDescriptionCoordinates pdc : pc.getClusterProblems(cluster)){
			if (!visited.contains(pdc.getCategory())){
				if (result.length()>0)
					result = result+", "+pdi.getProblemDefinition(pdc.getCategory()).getUri();
				else
					result = pdi.getProblemDefinition(pdc.getCategory()).getUri();
				visited.add(pdc.getCategory());
			}
		}
		return result;
	}
	
	private ArrayList<String> getActiveQuestions(ScreeningTest st){
		ArrayList<String> result = new ArrayList<String>();
		for (TestQuestion tq : st.getQuestionsList()){
			if (tq.isAttachRelWords() && !listContainsStringIgnoreCase(result, tq.getQuestion()))
				result.add(tq.getQuestion());
		}
		return result;
	}
	
	private boolean listContainsStringIgnoreCase(ArrayList<String> texts, String newText){
		for (String text : texts)
			if (text.equalsIgnoreCase(newText))
				return true;
		return false;
	}

	private class ClusterStats{
		int cluster, highScore, userScore;
		public ClusterStats(int cluster){
			this.cluster = cluster;
			this.highScore = 0;
			this.userScore = 0;
		}
		double getRate(){
			return (double)userScore/highScore;
		}
	}
	
	private ClusterStats getStatsObject(ArrayList<ClusterStats> stats, int cluster){
		int idx = 0;
		while (idx<stats.size() && stats.get(idx).cluster != cluster)
			idx++;
		if (idx >= stats.size())
			return null;
		return stats.get(idx);
	}
}
