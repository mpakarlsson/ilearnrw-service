package com.ilearnrw.services.profileAccessUpdater;

import ilearnrw.user.LanguageCode;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.UserProfile;
import ilearnrw.user.UserSeverities;
import ilearnrw.user.UserSeveritiesToProblems;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * 
 * @TODO Add more methods of accessing only the preferences, the severities etc.
 * @TODO Add POST versions to update the profile.
 * 		 - Needs verification of the severities, indices as well.
 * @TODO Switch Datasource we all use the same database.
 * @TODO Add token validation.
 * @TODO Update English once the dimensions of the severities/indices are known.
 * @TODO Consider storing a hash of the state of the profile so that we can
 * 		 detect conflicts between applications. The hash would be updated on each
 * 		 new update to the database.
 * @TODO Input verification in the Store function
 * @TODO Refactor, moving set/get functions out of the controller. Possible implement
 * 	     the IProfileProvider interface.
 * 
 * SQL
 * ---
 * 
 * The complete user profile is stored in one table. Since the size of the
 * severity_X_Y matrix and the index_X list will be dependent on the
 * ProblemDefinitionIndex we need 1 table for each language.
 * 
 * Note, that X, Y will range from 0 to the size of the ProblemDefinitionIndex.
 * 
 *	 		LC_Greek
 * ------------------------------
 *       userId | PK, VARCHAR(32)
 * prefFontSize | INTEGER
 * severity_X_Y | SHORT
 *      index_X | SHORT
 * 
 * 			LC_English
 * ------------------------------
 *       userId | PK, VARCHAR(32)
 * prefFontSize | INTEGER
 * severity_X_Y | SHORT
 *      index_X | SHORT
 *
 * In order to not have to supply the users language code a third table is required:
 * 
 * 		ProfileLanguage
 * ------------------------------------
 * 		 userId | PK, VARCHAR(32), NOT NULL
 * languageCode | INT, NOT NULL
 * 
 */
@Controller
public class ProfileAccessUpdaterController {
	
	@Autowired
	DataSource profileDataSource;
	static public String generateSQLTables() {
		StringBuilder ret = new StringBuilder();

		LC_Base[] languages = {new LC_Greek(), new LC_English()};
		for(LC_Base language : languages)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE ");
			sb.append(language.getTableName());
			sb.append(" (\n");
			sb.append("userid VARCHAR(32) NOT NULL PRIMARY KEY,\n");
			sb.append("prefFontSize INT,\n");

			for(int x=0; x<language.getProblemDefinitionIndexSize_X();x++)
			{
				for(int y=0; y < language.getProblemDefinitionIndexSizes_Y()[x]; y++)
				{
					sb.append("severity_");
					sb.append(x);
					sb.append("_");
					sb.append(y);
					sb.append(" TINYINT,\n");
				}
				sb.append("index_");
				sb.append(x);
				if(x < language.getProblemDefinitionIndexSize_X() - 1)
					sb.append(" TINYINT,\n");
				else
					sb.append(" TINYINT);");
				}
			ret.append(sb);
			ret.append("\n\n");
		}
		ret.append("CREATE TABLE ProfileLanguage (");
		ret.append("userId VARCHAR(32) NOT NULL PRIMARY KEY,");
		ret.append("languageCode TINYINT NOT NULL);");
		return ret.toString();
	}
	public interface LC_Base
	{
		public String getTableName();
		public Integer getLanguageCodeAsInt();
		public LanguageCode getLanguageCode();
		public Integer getProblemDefinitionIndexSize_X();
		public Integer[] getProblemDefinitionIndexSizes_Y();
	}
	static public class LC_Greek implements LC_Base
	{
		static final String TableName = "LC_Greek";
		static final Integer ProblemDefinitionIndexSize_X = 9;
		static final Integer[] ProblemDefinitionIndexSizes_Y = {20,12,6,13,19,6,20,7,10};
		@Override
		public String getTableName() {
			return TableName;
		}
		@Override
		public LanguageCode getLanguageCode() {
			return LanguageCode.GR;
		}
		@Override
		public Integer getLanguageCodeAsInt()
		{
			return (int) LanguageCode.getGreekCode();
		}
		@Override
		public Integer getProblemDefinitionIndexSize_X() {
			return ProblemDefinitionIndexSize_X;
		}
		@Override
		public Integer[] getProblemDefinitionIndexSizes_Y() {
			return ProblemDefinitionIndexSizes_Y;
		}
	};
	
	static public class LC_English implements LC_Base
	{
		static final String TableName = "LC_English";
		static final Integer ProblemDefinitionIndexSize_X = 9;
		static final Integer[] ProblemDefinitionIndexSizes_Y = {20,12,6,13,19,6,20,7,10};

		public String getTableName() { return TableName; }
		@Override
		public LanguageCode getLanguageCode() { return LanguageCode.EN; }
		@Override
		public Integer getLanguageCodeAsInt() { return (int) LanguageCode.getEnglishCode();
		}
		@Override
		public Integer getProblemDefinitionIndexSize_X() { return ProblemDefinitionIndexSize_X; }
		@Override
		public Integer[] getProblemDefinitionIndexSizes_Y() { return ProblemDefinitionIndexSizes_Y; }
	};
	
	/**
	 * Used to generate the SQL tables based of the information
	 * in the LC_Greek and LC_English classes.
	 * 
	 * Ensures that the database is in sync with the code in here.
	 * @return
	 */
	@RequestMapping(value = "/profile/generateSql", method = RequestMethod.GET)
	public @ResponseBody
	String generateSql() {
		return generateSQLTables();
	}

	/**
	 * Fetch a users preferences
	 * @param userId
	 * @return UserPreferences
	 */
	@RequestMapping(value = "/profile/preferences", method = RequestMethod.GET)
	public @ResponseBody
	UserPreferences getPreferences(@RequestParam(value = "userId", required = true) String userId) {
		return getProfileUsingDbLanguage(userId).getPreferences();
	}
	/**
	 * Fetch a users problem severities
	 * @param userId
	 * @return UserSeverities
	 */
	@RequestMapping(value = "/profile/problems", method = RequestMethod.GET)
	public @ResponseBody
	UserSeverities getProblems(@RequestParam(value = "userId", required = true) String userId) {
		return getProfileUsingDbLanguage(userId).getSeveritiesToProblemsMatrix().getUserSeverities();
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
	UserProfile getProfileUsingDbLanguage(@RequestParam(value = "userId", required = true) String userId) {
		return getProfile(userId, getLanguage(userId));
	}
	LC_Base getLanguage(LanguageCode languageCode)
	{
		if(languageCode == LanguageCode.EN)
			return new LC_English();
		return new LC_Greek();
	}
	LC_Base getLanguage(Integer languageCode)
	{
		if(languageCode == LanguageCode.getEnglishCode())
			return new LC_English();
		return new LC_Greek();
	}
	LC_Base getLanguage(String userId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(profileDataSource);
		final class LanguageCodeResult
		{
			public int languageCode;
		};
		final LanguageCodeResult languageCodeResult = new LanguageCodeResult();
		Object[] params = {userId};
		jdbcTemplate.query("SELECT languageCode FROM ProfileLanguage WHERE userId=?",
						   params,
						   new RowCallbackHandler() {
							@Override
							public void processRow(ResultSet rs) throws SQLException {
								languageCodeResult.languageCode = rs.getInt("languageCode");
							}
						});
		return getLanguage(languageCodeResult.languageCode);
	}

	/**
	 * Returns a complete User Profile using a language code supplied by the user.
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 */
	UserProfile getProfile(String userId,LC_Base languageCode) {
		final LC_Base language = languageCode;

		final UserSeverities userSeverities = new UserSeverities(language.getProblemDefinitionIndexSize_X());
		final UserSeveritiesToProblems severitiesToProblems = new UserSeveritiesToProblems();
		severitiesToProblems.setUserSeverities(userSeverities);
		final UserPreferences preferences = new UserPreferences();

		final JdbcTemplate jdbcTemplate = new JdbcTemplate(profileDataSource);
		Object[] params = {userId};
		jdbcTemplate.query("SELECT * FROM "+ language.getTableName()+" WHERE userId=?",
						   params,
					       new RowCallbackHandler() {
							@Override
							public void processRow(ResultSet rs)
									throws SQLException {
								/*Read preferences*/
								preferences.setFontSize(rs.getInt("prefFontSize"));
								/*Read severities and indices.*/
								for(int x = 0; x < language.getProblemDefinitionIndexSize_X(); x++)
								{
									userSeverities.setIndex(x, rs.getInt(String.format("index_%s", x)));
									userSeverities.constructRow(x, language.getProblemDefinitionIndexSizes_Y()[x]);
									for(int y = 0; y < language.getProblemDefinitionIndexSizes_Y()[x]; y++)
									{
										userSeverities.setSeverity(x, y, rs.getInt(String.format("severity_%s_%s", x,y)));
									}
								}
							}});
		return new UserProfile(language.getLanguageCode(),
				severitiesToProblems,
				preferences);
	}
	
	
	/**
	 * Temporary test function that can be used to increment a problem severity.
	 * @param userId
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/profile/problem_increment", method = RequestMethod.GET)
	public @ResponseBody
	UserProfile IncrementUserProfileSeverity(@RequestParam(value = "userId", required = true) String userId,
											 @RequestParam(value="x", required=true) int x,
											 @RequestParam(value="y", required=true) int y
											 ) throws Exception
	{
		UserProfile profile = getProfile(userId, getLanguage(userId));
		profile.getSeveritiesToProblemsMatrix().getUserSeverities().setSeverity(x, y,
				profile.getSeveritiesToProblemsMatrix().getUserSeverities().getSeverity(x, y) + 1);
		storeProfile(userId, profile);
		return getProfile(userId, getLanguage(userId));
	}
	
	void storeProfile(String userId, UserProfile profile) throws Exception
	{
		final LC_Base language = getLanguage(profile.getLanguage());
		final class ReplaceQuery {
			public void add(String name, Object value){
				params.add(name);
				values.add(value);
			}
			public String getQuery(LC_Base language) throws Exception {
				StringBuilder sql = new StringBuilder("REPLACE INTO " + language.getTableName());
				sql.append(" (");
				sql.append(getParamString());
				sql.append(")VALUES(");
				sql.append(getValueString());
				sql.append(")");
				return sql.toString();
			}
			private void validateLists() throws Exception
			{
				if( params.size() != values.size())
					throw new Exception("The size of params must be the same as values,");
			}
			private String getParamString() throws Exception {
				validateLists();
				StringBuilder sql = new StringBuilder();
				Iterator<String> itr = params.iterator();
			    while (itr.hasNext()) {
					sql.append(itr.next());
					if( itr.hasNext())
						sql.append(",");
			    }
			    return sql.toString();
			}
			private String getValueString() throws Exception {
				validateLists();
				StringBuilder sql = new StringBuilder();
				Iterator<Object> itr = values.iterator();
			    while (itr.hasNext()) {
					itr.next();
					if( itr.hasNext())
						sql.append("?,");
					else
						sql.append("?");
			    }
			    return sql.toString();
			}
			public Object[] getParams() {
				return values.toArray();
			}
			List<String> params = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
		};
		
		ReplaceQuery replaceQuery = new ReplaceQuery();
		replaceQuery.add("userId", userId);
		replaceQuery.add("prefFontSize", profile.getPreferences().getFontSize());
		for(int x = 0; x < language.getProblemDefinitionIndexSize_X(); x++)
		{
			replaceQuery.add(String.format("index_%s", x), profile.getSeveritiesToProblemsMatrix().getIndex(x));
			for(int y = 0; y < language.getProblemDefinitionIndexSizes_Y()[x]; y++)
			{
				replaceQuery.add(String.format("severity_%s_%s", x,y),
								 profile.getSeveritiesToProblemsMatrix().getSeverity(x, y));
			}
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate(profileDataSource);
		jdbcTemplate.update(replaceQuery.getQuery(language),
							replaceQuery.getParams());
	}
}
