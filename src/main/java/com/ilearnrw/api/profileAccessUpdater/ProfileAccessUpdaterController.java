package com.ilearnrw.api.profileAccessUpdater;

import java.util.ArrayList;
import java.util.List;

import ilearnrw.textclassification.Word;
import ilearnrw.user.UserDetails;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.UserSeverities;
import ilearnrw.utils.LanguageCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.ExpertTeacherService;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

/**
 * @TODO Add more methods of accessing only the preferences, the severities etc.
 * @TODO Add POST versions to update the profile. - Needs verification of the
 *       severities, indices as well.
 * @TODO Add token validation.
 * @TODO Consider storing a hash of the state of the profile so that we can
 *       detect conflicts between applications. The hash would be updated on
 *       each new update to the database.
 * @TODO Input verification in the Store function
 * @TODO Documentation
 */
@Controller
public class ProfileAccessUpdaterController {

	@Autowired
	IProfileProvider profileProvider;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private TeacherStudentService teacherStudentService;

	@Autowired
	private ExpertTeacherService expertTeacherService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;


	/**
	 * Used to generate the SQL tables based of the information in the LC_Greek
	 * and LC_English classes.
	 * 
	 * Ensures that the database is in sync with the code in here.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/profile/generateSql", method = RequestMethod.GET)
	public @ResponseBody
	String generateSql() {
		return DbProfileProvider.generateSQLTables();
	}

	/**
	 * Fetch a users preferences
	 * 
	 * @param userId
	 * @return UserPreferences
	 */
	@RequestMapping(value = "/profile/preferences", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	UserPreferences getPreferences(
			@RequestParam(value = "userId", required = true) int userId)
			throws ProfileProviderException {
		return profileProvider.getProfile(userId).getPreferences();
	}

	/**
	 * Fetch the problem definitions for an user's language
	 * 
	 * @param userId
	 * @return UserSeverities
	 */
	@RequestMapping(value = "/profile/problemDefinitions", method = RequestMethod.GET)
	public @ResponseBody
	ProblemDefinitionIndex getProblemDefinitions(
			@RequestParam(value = "userId", required = true) int userId)
			throws ProfileProviderException {
		LanguageCode lCode = profileProvider.getProfile(userId).getLanguage();
		return new ProblemDefinitionIndex(lCode);
	}
	
	/**
	 * Fetch a users problem severities
	 * 
	 * @param userId
	 * @return UserSeverities
	 */
	@RequestMapping(value = "/profile/problems", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	UserSeverities getProblems(
			@RequestParam(value = "userId", required = true) int userId)
			throws ProfileProviderException {
		return profileProvider.getProfile(userId).getUserProblems()
				.getUserSeverities();
	}

	/**
	 * Returns a complete User Profile using the language code from the
	 * database.
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	UserProfile getProfile(
			@RequestParam(value = "userId", required = true) int userId)
			throws ProfileProviderException {
		return profileProvider.getProfile(userId);
	}
	
	/**
	 * Test function of create
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 * @throws ProfileProviderException
	 */
	@RequestMapping(value = "/profile/create", method = RequestMethod.GET)
	public @ResponseBody
	String createProfile(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "languageCode", required = true) String languageCode)
			throws ProfileProviderException {
		profileProvider.createProfile(userId, LanguageCode.fromString(languageCode));
		return "ok";
	}

	/**
	 * Test function of delete
	 * 
	 * @param userId
	 * @return
	 * @throws ProfileProviderException
	 */
	@RequestMapping(value = "/profile/delete", method = RequestMethod.GET)
	public @ResponseBody
	String deleteProfile(
			@RequestParam(value = "userId", required = true) int userId)
			throws ProfileProviderException {
		profileProvider.deleteProfile(userId);
		return "ok";
	}

	@RequestMapping(value = "/profile/update", method = RequestMethod.POST)
	//@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	ArrayList<UpdatedProfileEntry> updateProfile(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "category", required = false) Integer category,
			@RequestParam(value = "index", required = false) Integer index)
			throws ProfileProviderException {
		ArrayList<UpdatedProfileEntry> updates = new ArrayList<UpdatedProfileEntry>();
		if (category == null || index == null){
			updates = profileProvider.updateTheProfileAutomatically(userId, 20);
		}
		else{
			updates.add(profileProvider.updateProfileEntry(userId, category, index, 20));
		}
		
		profileProvider.getProfile(userId).getUserProblems().updateSystemCluster();
		
		return updates;
	}

	@RequestMapping(value = "/profile/set_new", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	ArrayList<UpdatedProfileEntry> setProfile(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestBody UserProfile newProfile)
			throws ProfileProviderException {
		ArrayList<UpdatedProfileEntry> updates = new ArrayList<UpdatedProfileEntry>();
		updates = profileProvider.updateProfile(userId, newProfile);
		return updates;
	}

	/**
	 * Temporary test function that can be used to increment a problem severity.
	 * 
	 * @param userId
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/profile/problem_increment", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody
	UserProfile IncrementUserProfileSeverity(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "x", required = true) int x,
			@RequestParam(value = "y", required = true) int y)
			throws ProfileProviderException {
		UserProfile profile = profileProvider.getProfile(userId);
		profile.getUserProblems().setUserSeverity(
						x, y,
						profile.getUserProblems().getUserSeverity(x, y) + 1);
		profileProvider.updateProfile(userId, profile);
		return profileProvider.getProfile(userId);
	}
	
	/**
	 * Adds a tricky word 
	 * 
	 * @param userId
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/profile/trickywords/add", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#userId, 'READ_PROFILE')")
	public @ResponseBody String addTrickyWord(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "word", required = true) String word)
			throws ProfileProviderException {
		UserProfile profile = profileProvider.getProfile(userId);
		profile.getUserProblems().getTrickyWords().add(new Word(word));
		profileProvider.updateProfile(userId, profile);
		return "ok";
	}
	
	/**
	 * Deletes a tricky word 
	 * 
	 * @param userId
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/profile/trickywords/delete", method = RequestMethod.GET)
	public @ResponseBody String deleteTrickyWord(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "word", required = true) String word)
			throws ProfileProviderException {
		UserProfile profile = profileProvider.getProfile(userId);
		if (profile.getUserProblems().getTrickyWords().remove(new Word(word)))
			profileProvider.updateProfile(userId, profile);
		else
			return "word not found";
		return "ok";
	}
	
	boolean userHasRole(User user, String permission)
	{
		List<Role> roles = roleService.getRoleList(user);
		List<Permission> permissions = new ArrayList<Permission>();
		for (Role role : roles)
			permissions.addAll(permissionService.getPermissionList(role));
		return permissions.contains(permissionService.getPermission(permission));
	}
	
	@RequestMapping(value = "application/{userId}/profiles", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	List<User> getProfilesForApplication(@PathVariable int userId) {
		User user = userService.getUser(userId);
		if (user == null)
			return new ArrayList<User>();
		if (userHasRole(user, "PERMISSION_ADMIN"))
			return teacherStudentService.getAllStudentsList();
		if (userHasRole(user, "PERMISSION_EXPERT"))
		{
			List<User> teachers = expertTeacherService.getTeacherList(user);
			List<User> students = new ArrayList<User>();
			for (User teacher : teachers)
				students.addAll(teacherStudentService.getStudentList(teacher));
			return students;
		}
		if (userHasRole(user, "PERMISSION_TEACHER"))
		{
			return teacherStudentService.getStudentList(user);
		}
		if (userHasRole(user, "PERMISSION_STUDENT"))
		{
			List<User> students = new ArrayList<User>();
			students.add(user);
			return students;
		}
		return new ArrayList<User>();
	}
}
