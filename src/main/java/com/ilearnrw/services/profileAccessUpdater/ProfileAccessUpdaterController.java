package com.ilearnrw.services.profileAccessUpdater;

import java.util.List;

import ilearnrw.user.UserDetails;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.UserSeverities;
import ilearnrw.utils.LanguageCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.services.profileAccessUpdater.IProfileProvider.ProfileProviderException;

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
	public @ResponseBody
	UserPreferences getPreferences(
			@RequestParam(value = "userId", required = true) String userId)
			throws ProfileProviderException {
		return profileProvider.getProfile(userId).getPreferences();
	}

	/**
	 * Fetch a users problem severities
	 * 
	 * @param userId
	 * @return UserSeverities
	 */
	@RequestMapping(value = "/profile/problems", method = RequestMethod.GET)
	public @ResponseBody
	UserSeverities getProblems(
			@RequestParam(value = "userId", required = true) String userId)
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
	public @ResponseBody
	UserProfile getProfile(
			@RequestParam(value = "userId", required = true) String userId)
			throws ProfileProviderException {
		return profileProvider.getProfile(userId);
	}
	
	@RequestMapping(value = "/profile/details", method = RequestMethod.GET)
	public @ResponseBody
	UserDetails getDetails(
			@RequestParam(value = "userId", required = true) String userId)
			throws ProfileProviderException {
		return profileProvider.getDetails(userId);
	}
	
	@RequestMapping(value = "/profile/list", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getUserIdList()
			throws ProfileProviderException {
		return profileProvider.getUserIdList();
	}
	
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#id, 'READ_PROFILE')")
	public @ResponseBody
	String getProfile(
			@PathVariable int id) {
		return "access to profile with id " + id + " granted!";
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
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "languageCode", required = true) Integer languageCode)
			throws ProfileProviderException {
		if (languageCode == 1)
			profileProvider.createProfile(userId, LanguageCode.EN);
		else
			profileProvider.createProfile(userId, LanguageCode.GR);
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
			@RequestParam(value = "userId", required = true) String userId)
			throws ProfileProviderException {
		profileProvider.deleteProfile(userId);
		return "ok";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public @ResponseBody
	String updateProfile(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestBody UserProfile newProfile)
			throws ProfileProviderException {
		profileProvider.updateProfile(userId, newProfile);
		return "ok";
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
	public @ResponseBody
	UserProfile IncrementUserProfileSeverity(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "x", required = true) int x,
			@RequestParam(value = "y", required = true) int y)
			throws ProfileProviderException {
		UserProfile profile = profileProvider.getProfile(userId);
		profile.getUserProblems()
				.getUserSeverities()
				.setSeverity(
						x,
						y,
						profile.getUserProblems().getUserSeverities()
								.getSeverity(x, y) + 1);
		profileProvider.updateProfile(userId, profile);
		return profileProvider.getProfile(userId);
	}
}
