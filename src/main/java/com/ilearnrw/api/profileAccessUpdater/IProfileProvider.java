package com.ilearnrw.api.profileAccessUpdater;

import java.util.List;

import ilearnrw.user.UserDetails;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

/**
 * @author David
 * 
 * CRUD interface for profiles.
 * 
 * I've skipped some function from the specification such as:
 * 
 * 		getProfilePreferences
 * 		getProfileProblems
 * 		updateProfilePreference
 * 		updateProfileProblem
 * 
 * The reason being that these can easily be extracted from the
 * UserProfile by the caller.
 * 
 * I've also added a Delete function.
 * 
 * The create function also a requires a LanguageCode.
 * I don't know how you would like to do if the LanguageCode changes in
 * the profile in an updateProfile. ATM the languagecode in the UserProfile
 * will be ignored and the code supplied when originally creating the
 * profile will be used.
 */
public interface IProfileProvider {
	public class ProfileProviderException extends Throwable
	{
		private static final long serialVersionUID = 1L;
		public ProfileProviderException(Type t, String message_) {
			type = t;
			message = message_;
		}
		
		public enum Type {
			generalFailure,
			userDoesNotExist,
			failedToCreateUser;
		};
		
		public Type type;
		public String message;
		
	}
	/**
	 * @param userId
	 * @return Returns a UserProfile if available. 
	 */
	UserProfile getProfile(String userId) throws ProfileProviderException;
	/**
	 * Updates a profile with the values from "newProfile"
	 * @param userId
	 * @param newProfile
	 * @throws ProfileProviderException
	 */
	void updateProfile(String userId, UserProfile newProfile) throws ProfileProviderException;
	/**
	 * Updates a 'cell' of the user's profile based on the logs stored on datalogger
	 * @param userId
	 * @param category
	 * @param index
	 * @throws ProfileProviderException
	 */
	void updateProfileEntry(String userId, int category, int index, int threshold) 
			throws ProfileProviderException;
	/**
	 * Updates the user's profile based on the logs stored on datalogger
	 * @param userId
	 * @throws ProfileProviderException
	 */
	void updateTheProfileAutomatically(String userId, int threshold) 
			throws ProfileProviderException;
	/**
	 * Creates a new profile
	 * @param userId
	 * @param languageCode
	 * @throws ProfileProviderException
	 */
	void createProfile(String userId, LanguageCode languageCode) throws ProfileProviderException;
	/**
	 * Deletes a profile
	 * @param userId
	 * @throws ProfileProviderException
	 */
	void deleteProfile(String userId) throws ProfileProviderException;

	void createTables();
}
