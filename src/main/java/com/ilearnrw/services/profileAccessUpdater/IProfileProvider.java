package com.ilearnrw.services.profileAccessUpdater;

import javax.sql.DataSource;

import ilearnrw.user.LanguageCode;
import ilearnrw.user.UserProfile;

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
}
