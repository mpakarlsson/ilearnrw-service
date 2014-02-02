package com.ilearnrw.api.datalogger.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author David Johansson
 * 
 *         DataLogger Log object.
 * 
 *         As specified in D6.1_ILearnRW_System_Architecture_final.
 * 
 * 
 * 
 */
public class LogEntry implements Serializable {

	public LogEntry(String username, String applicationId, Timestamp timestamp,
			String tag, String word, int problemCategory, int problemIndex,
			float duration, String level, String mode, String value) {

		this.username = username;
		this.applicationId = applicationId;
		this.tag = tag;
		this.word = word;
		this.problemCategory = problemCategory;
		this.problemIndex = problemIndex;
		this.duration = duration;
		this.level = level;
		this.mode = mode;
		this.value = value;
		this.timestamp = timestamp;

	}

	/**
	 * We need the default constructor in order to serialize FROM json to this
	 * object.
	 */
	public LogEntry() {
	}

	private static final long serialVersionUID = -7085054143765991532L;

	/**
	 * The user-id for the user that the particular log concerns.
	 */
	@JsonProperty("username")
	private String username;

	/**
	 * Tags for the LogEntry.
	 */
	@JsonProperty("tag")
	@Valid
	@Pattern(regexp = "^[A-Z0-9_]+$")
	private String tag;

	/**
	 * A value stored with the log.
	 * 
	 */
	@JsonProperty("value")
	private String value;

	/**
	 * An application identifier.
	 */
	@JsonProperty("applicationId")
	private String applicationId;

	@JsonProperty("timestamp")
	private Timestamp timestamp;

	@JsonProperty("word")
	private String word;

	@JsonProperty("problem_category")
	private int problemCategory;

	@JsonProperty("problem_index")
	private int problemIndex;

	@JsonProperty("duration")
	private float duration;

	@JsonProperty("level")
	private String level;

	@JsonProperty("mode")
	private String mode;

	public String getUsername() {
		return username;
	}

	public String getTag() {
		if (tag == null)
			return tag;
		
		return tag.toUpperCase();
	}

	public String getValue() {
		return value;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public Timestamp getTimestamp() {
		if (timestamp != null)
			return timestamp;

		// we return the server's time if not set by application
		return new Timestamp(new Date().getTime());
	}

	public String getWord() {
		return word;
	}

	public int getProblemCategory() {
		return problemCategory;
	}

	public int getProblemIndex() {
		return problemIndex;
	}

	public float getDuration() {
		return duration;
	}

	public String getLevel() {
		return level;
	}

	public String getMode() {
		return mode;
	}

}
