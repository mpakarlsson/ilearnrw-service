package com.ilearnrw.services.profileAccessUpdater;

import ilearnrw.user.LanguageCode;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.UserSeveritiesToProblems;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;

import ilearnrw.user.*;

/** 
 * 
 * @TODO Add more methods of accessing only the preferences, the severities etc.
 * @TODO Add POST versions to update the profile.
 * 		 - Needs verification of the severities, indices as well.
 * @TODO Switch Datasource we all use the same database.
 * @TODO Add token validation.
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
	 * Returns a complete User Profile using the language code from the
	 * database.
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 */
	@RequestMapping(value = "/profile/user/{userId}", method = RequestMethod.GET)
	public @ResponseBody
	UserProfile getProfileUsingDbLanguage(@PathVariable String userId) {
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
		return getProfile(userId, languageCodeResult.languageCode);
	}
	/**
	 * Returns a complete User Profile using a language code supplied by the user.
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 */
	@RequestMapping(value = "/profile/{userId}/{languageCode}", method = RequestMethod.GET)
	public @ResponseBody
	UserProfile getProfile(@PathVariable String userId, @PathVariable Integer languageCode) {

		LC_Base lang_sel = null;
		if(languageCode == LanguageCode.getEnglishCode())
			lang_sel = new LC_English();
		else
			lang_sel = new LC_Greek();
		final LC_Base language = lang_sel;


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
		return new UserProfile(lang_sel.getLanguageCode(),
				severitiesToProblems,
				preferences);
	}
}
