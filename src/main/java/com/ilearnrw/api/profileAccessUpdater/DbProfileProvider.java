package com.ilearnrw.api.profileAccessUpdater;

import ilearnrw.textclassification.Word;
import ilearnrw.user.UserDetails;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.profile.UserProblems;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.UserSeverities;
import ilearnrw.utils.LanguageCode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.lf5.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import com.ilearnrw.api.datalogger.DataloggerController;
import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.datalogger.services.CubeServiceImpl;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException.Type;


/** 
 * @TODO Switch Datasource we all use the same database.
 * @TODO Update English once the dimensions of the severities/indices are known.
 * @TODO Input verification in the Store function
 * @TODO Documentation
 * SQL
 * ---
 * 
 * The complete user profile is stored in one table. Since the size of the
 * severity_X_Y matrix and the system_index_X, teacher_index_X list will be dependent on the
 * ProblemDefinitionIndex we need 1 table for each language.
 * 
 * Note, that X, Y will range from 0 to the size of the ProblemDefinitionIndex.
 * 
 *	 		LC_Greek
 * ------------------------------
 *          userId | PK, BIGINT
 *    prefFontSize | INTEGER
 *    severity_X_Y | SHORT
 *  system_index_X | SHORT
 * teacher_index_X | SHORT
 * 
 * 			LC_English
 * ------------------------------
 *          userId | PK, BIGINT
 *    prefFontSize | INTEGER
 *    severity_X_Y | SHORT
 *  system_index_X | SHORT
 * teacher_index_X | SHORT
 *
 * In order to not have to supply the users language code a third table is required:
 * 
 * 		ProfileLanguage
 * ------------------------------------
 * 		 userId | PK, BIGINT, NOT NULL
 * languageCode | INT, NOT NULL
 * 
 * The tricky words are stored in a separate table
 * 
 * 		TrickyWords
 * ------------------------------------
 * 		 userId | BIGINT, NOT NULL
 *         word | VARCHAR(45)
 * 
 */
@Service
public class DbProfileProvider implements IProfileProvider {

	@Autowired
	DataSource profileDataSource;
	@Autowired
	DataSource usersDataSource;
	
	@Autowired
	CubeService cubeService;
	
	@Override
	public UserProfile getProfile(int userId)
			throws ProfileProviderException {
		return getProfile(userId, getLanguage(String.valueOf(userId)));
	}

	@Override
	public UpdatedProfileEntry updateProfileEntry(int userId, int category, int index, int threshold)
			throws ProfileProviderException {
		try {
			UserProfile up = getProfile(userId);
			String username = cubeService.getUsername(userId);
			ListWithCount<WordSuccessCount> l = 
					cubeService.getWordsByProblemAndSessions(username, 
							category, index, null, null, 20, true);
			List<WordSuccessCount> thelist = l.getList();
			
			int SuccessSum = 0, FailSum = 0, count = 0;
			for (WordSuccessCount wc : thelist){
				count += wc.getCount();
				SuccessSum += wc.getSucceed();
				FailSum += wc.getFailed();
			}
			if (count<threshold)
				return null;
			UpdatedProfileEntry update = new UpdatedProfileEntry(category, index, 
					up.getUserProblems().getUserSeverity(category, index), -1);
			double pcnt = ( (double)SuccessSum ) /(SuccessSum+FailSum);
			if (pcnt >0.95)
				up.getUserProblems().setUserSeverity(category, index, 0);
			else if (pcnt >0.80)
				up.getUserProblems().setUserSeverity(category, index, 1);
			else if (pcnt >0.65)
				up.getUserProblems().setUserSeverity(category, index, 2);
			else 
				up.getUserProblems().setUserSeverity(category, index, 3);
			update.setNewSeverity(up.getUserProblems().getUserSeverity(category, index));
			storeProfile(userId, up);
			if (update.getNewSeverity() != update.getPreviousSeverity())
				return update;
		} catch(Exception e){
			System.err.println(e.toString());
		}
		catch (QueryParamException e) {
			throw new ProfileProviderException(Type.generalFailure, "Could not store the user profile");
		}
		return null;
	}

	@Override
	public ArrayList<UpdatedProfileEntry> updateTheProfileAutomatically(int userId, int threshold)
			throws ProfileProviderException {
		ArrayList<UpdatedProfileEntry> updates = new ArrayList<UpdatedProfileEntry>();
		UserProfile up = getProfile(userId);
		for (int i=0; i<up.getUserProblems().getNumerOfRows(); i++){
			for (int j=0;j<up.getUserProblems().getRowLength(i); j++){
				UpdatedProfileEntry t = updateProfileEntry(userId, i, j, threshold);
				if (t!=null)
					updates.add(t);
			}
		}
		return updates;
	}

	@Override
	public ArrayList<UpdatedProfileEntry> updateProfile(int userId, UserProfile newProfile)
			throws ProfileProviderException {
		try {
			ArrayList<UpdatedProfileEntry> updates = new ArrayList<UpdatedProfileEntry>();
			UserProfile up = getProfile(userId);
			for (int i=0; i<up.getUserProblems().getNumerOfRows(); i++){
				for (int j=0;j<up.getUserProblems().getRowLength(i); j++){
					UpdatedProfileEntry t = new UpdatedProfileEntry(i, j, 
							up.getUserProblems().getUserSeverity(i, j), 
							newProfile.getUserProblems().getUserSeverity(i, j));
					if (t.getNewSeverity() != t.getPreviousSeverity())
						updates.add(t);
				}
			}
			storeProfile(userId, newProfile);
			return updates;
		} catch (QueryParamException e) {
			throw new ProfileProviderException(Type.generalFailure, "Could not store the user profile");
		}

	}

	@Override
	public void createProfile(int userId, LanguageCode languageCode)
			throws ProfileProviderException {
		JdbcTemplate template = new JdbcTemplate(profileDataSource);
		int i = template.update("INSERT INTO ProfileLanguage (userId, languageCode)VALUES(?,?)",
				userId, getLanguage(languageCode).getLanguageCodeAsInt());
		if( i != 1)
			throw new ProfileProviderException(Type.failedToCreateUser, "SQL Insert did not return 1 row affected.");
		/* Small hack to insert a default row in the database.
		 */
		try {
			storeProfile( userId, getProfile(userId, getLanguage(languageCode)));
		} catch (QueryParamException e) {
			throw new ProfileProviderException(Type.generalFailure, "Could not store the user profile");
		}
	}

	@Override
	public void deleteProfile(int userId) throws ProfileProviderException {
		JdbcTemplate template = new JdbcTemplate(profileDataSource);
		int i = template.update("DELETE FROM ProfileLanguage WHERE userId=?", userId);
		if( i != 1)
			throw new ProfileProviderException(Type.userDoesNotExist, "SQL Delete did not return 1 row affected.");
	}
	
	public void createTables() {
		JdbcTemplate template = new JdbcTemplate(profileDataSource);
		Resource resource = new ByteArrayResource(generateSQLTables().getBytes());
		JdbcTestUtils.executeSqlScript(template, resource, true);
	}
	
	static public String generateSQLTables() {
		StringBuilder ret = new StringBuilder();

		LC_Base[] languages = {new LC_Greek(), new LC_English()};
		for(LC_Base language : languages)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE IF NOT EXISTS ");
			sb.append(language.getTableName());
			sb.append(" (\n");
			sb.append("userid BIGINT NOT NULL PRIMARY KEY,\n");
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
				sb.append("system_index_");
				sb.append(x);
				sb.append(" TINYINT,\n");
				sb.append("teacher_index_");
				sb.append(x);
				if(x < language.getProblemDefinitionIndexSize_X() - 1)
					sb.append(" TINYINT,\n");
				else
					sb.append(" TINYINT);");
				}
			ret.append(sb);
			ret.append("\n\n");
		}
		ret.append("CREATE TABLE IF NOT EXISTS ProfileLanguage (");
		ret.append("userId BIGINT NOT NULL PRIMARY KEY,");
		ret.append("languageCode TINYINT NOT NULL);\n");
		ret.append("CREATE TABLE IF NOT EXISTS TrickyWords (");
		ret.append("userId BIGINT NOT NULL,");
		ret.append("word VARCHAR(45) NULL);");
		return ret.toString();
	}
	private interface LC_Base
	{
		public String getTableName();
		public Integer getLanguageCodeAsInt();
		public LanguageCode getLanguageCode();
		public Integer getProblemDefinitionIndexSize_X();
		public Integer[] getProblemDefinitionIndexSizes_Y();
	}
	static private class LC_Greek implements LC_Base
	{
		static final String TableName = "LC_Greek";
		static final Integer ProblemDefinitionIndexSize_X = 9;
		static final Integer[] ProblemDefinitionIndexSizes_Y = {20,12,5,13,17,6,26,10,8};
		@Override
		public String getTableName() { return TableName; }
		@Override
		public LanguageCode getLanguageCode() { return LanguageCode.GR; }
		@Override
		public Integer getLanguageCodeAsInt() { return (int) LanguageCode.getGreekCode(); }
		@Override
		public Integer getProblemDefinitionIndexSize_X() { return ProblemDefinitionIndexSize_X; }
		@Override
		public Integer[] getProblemDefinitionIndexSizes_Y() { return ProblemDefinitionIndexSizes_Y; }
	};
	static private class LC_English implements LC_Base
	{
		static final String TableName = "LC_English";
		static final Integer ProblemDefinitionIndexSize_X = 6;
		static final Integer[] ProblemDefinitionIndexSizes_Y = {37,72,61,40,57,127};

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
	LC_Base getLanguage(String userId) throws ProfileProviderException {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(profileDataSource);
		final class LanguageCodeResult
		{
			public int languageCode = -1;
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
		if( languageCodeResult.languageCode == -1 )
			throw new ProfileProviderException(Type.userDoesNotExist, "User not found in ProfileLanguage table.");
		return getLanguage(languageCodeResult.languageCode);
	}
	
	
	public class QueryParamException extends Throwable
	{}
	
	

	/**
	 * Returns a complete User Profile using a language code supplied by the user.
	 * 
	 * @param userId
	 * @param languageCode
	 * @return
	 */
	UserProfile getProfile(int userId, LC_Base languageCode) {
		final LC_Base language = languageCode;

		final UserSeverities userSeverities = new UserSeverities(language.getProblemDefinitionIndexSize_X());
		final UserProblems userProblems = new UserProblems(languageCode.getLanguageCode());

		userProblems.setUserSeverities(userSeverities);
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
									userProblems.setSystemIndex(x, rs.getInt(String.format("system_index_%s", x)));
									userProblems.setTeacherIndex(x, rs.getInt(String.format("teacher_index_%s", x)));
									userSeverities.constructRow(x, language.getProblemDefinitionIndexSizes_Y()[x]);
									for(int y = 0; y < language.getProblemDefinitionIndexSizes_Y()[x]; y++)
									{
										userProblems.setUserSeverity(x, y, rs.getInt(String.format("severity_%s_%s", x,y)));
									}
								}
							}});

		final List<Word> trickyWords = new ArrayList<Word>();
		jdbcTemplate.query("SELECT word FROM TrickyWords WHERE userId=?",
				params,
				new RowCallbackHandler() {
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						trickyWords.add(new Word(rs.getString("word")));
					}
				});
		userProblems.setTrickyWords(trickyWords);
		return new UserProfile(language.getLanguageCode(),
				userProblems,
				preferences);
	}

	void storeProfile(final int userId, final UserProfile profile) throws QueryParamException
	{
		final LC_Base language = getLanguage(profile.getLanguage());
		final class ReplaceQuery {
			public void add(String name, Object value){
				params.add(name);
				values.add(value);
			}
			public String getQuery(LC_Base language) throws QueryParamException {
				StringBuilder sql = new StringBuilder("REPLACE INTO " + language.getTableName());
				sql.append(" (");
				sql.append(getParamString());
				sql.append(")VALUES(");
				sql.append(getValueString());
				sql.append(")");
				return sql.toString();
			}
			private void validateLists() throws QueryParamException
			{
				if( params.size() != values.size())
					throw new QueryParamException();
			}
			private String getParamString() throws QueryParamException{
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
			private String getValueString() throws QueryParamException{
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
			replaceQuery.add(String.format("system_index_%s", x), profile.getUserProblems().getSystemIndex(x));
			replaceQuery.add(String.format("teacher_index_%s", x), profile.getUserProblems().getTeacherIndex(x));
			for(int y = 0; y < language.getProblemDefinitionIndexSizes_Y()[x]; y++)
			{
				try {
					replaceQuery.add(String.format("severity_%s_%s", x,y),
								 profile.getUserProblems().getUserSeverity(x, y));
				} catch (java.lang.NullPointerException ex) {
					replaceQuery.add(String.format("severity_%s_%s", x,y),
								 0); //Default to 0 when there is no severity.
				}
			}
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate(profileDataSource);
		jdbcTemplate.update(replaceQuery.getQuery(language),
							replaceQuery.getParams());
		Object[] params = {userId};
		jdbcTemplate.update("DELETE FROM TrickyWords WHERE userId = ?", params);
		jdbcTemplate.batchUpdate("INSERT INTO TrickyWords (userId, word) VALUES (?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, userId);
				ps.setString(2, profile.getUserProblems().getTrickyWords().get(i).getWord());
			}
			
			@Override
			public int getBatchSize() {
				return profile.getUserProblems().getTrickyWords().size();
			}
		});
	}
}
