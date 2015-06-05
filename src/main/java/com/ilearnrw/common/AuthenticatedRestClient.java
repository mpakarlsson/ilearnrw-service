package com.ilearnrw.common;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.problems.ProblemDefinitionIndex;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.ilearnrw.api.datalogger.model.LogEntryResult;
import com.ilearnrw.api.datalogger.services.CubeServiceImpl;
import com.ilearnrw.common.security.Tokens;
import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.User;

@Component
public class AuthenticatedRestClient {
	private RestTemplate template;
	private HttpHeaders headers;
	
	private static Logger LOG = Logger.getLogger(CubeServiceImpl.class);

	@Value("${auth.baseurl}")
	private String authBaseUri;

	@Value("${logs.baseurl}")
	private String logsBaseUri;
	
	@Value("${profile.baseurl}")
	private String profileBaseUri;

	private String rolesUri = "user/roles?token={token}";
	private String userDetailsUri = "user/details/{username}";
	private String userClassroomUri = "user/details/{username}/classroom";
	private String userSchoolUri = "user/details/{username}/school";
	private String authUri = "user/auth?username={username}&pass={pass}";
	private String logsUri = "logs/{username}?page={page}";
	private String problemDefinitionsUri = "profile/problemDefinitions?userId={userId}";
	private String profileCreateUri = "profile/create?userId={userId}&languageCode={languageCode}";

	private static class NullHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public AuthenticatedRestClient() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				// No need to implement.
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				// No need to implement.
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection
					.setDefaultHostnameVerifier(new NullHostnameVerifier());
			SSLContext.setDefault(sc);
		} catch (Exception e) {
			System.out.println(e);
		}

		RestTemplate restTemplate = new RestTemplate();
		this.template = restTemplate;
		
		this.headers = new HttpHeaders() {
			{
				byte[] encodedAuthorisation = Base64.encode(new String(
						"api:api").getBytes());
				add("Authorization", "Basic "
						+ new String(encodedAuthorisation));
			}
		};
	}

	public <T> T get(String url, Class<T> responseType, Object... urlVariables) {
		LOG.debug(url);
		URI expanded = new UriTemplate(url).expand(urlVariables);
		ResponseEntity<T> response = template.exchange(expanded,
				HttpMethod.GET, new HttpEntity<Object>(headers), responseType);

		return response.getBody();
	}

	public Tokens getTokens(String username, String pass) {
		return get(getAuthUri(), Tokens.class, username, pass);
	}
	
	public String getUserDetailsUri() {
		return authBaseUri + userDetailsUri;
	}
	
	public String getUserClassroomUri() {
		return authBaseUri + userClassroomUri;
	}
	
	public String getUserSchoolUri() {
		return authBaseUri + userSchoolUri;
	}

	public String getRolesUri() {
		return authBaseUri + rolesUri;
	}

	public String getAuthUri() {
		return authBaseUri + authUri;
	}

	public String getLogsUri() {
		return logsBaseUri + logsUri;
	}
	
	public String getProblemDefinitionsUri() {
		return profileBaseUri + problemDefinitionsUri ;
	}

	private String getProfileCreateUri() {
		return profileBaseUri + profileCreateUri;
	}

	public User getUserDetails(String username) {
		return get(getUserDetailsUri(), User.class, username);
	}
	
	public Classroom getUserClassroom(String username) {
		return get(getUserClassroomUri(), Classroom.class, username);
	}
	
	public School getUserSchool(String username) {
		return get(getUserSchoolUri(), School.class, username);
	}

	public List<String> getRoles(String token) {
		List list = this.get(getRolesUri(), List.class, token);
		List<String> result = new ArrayList<String>();
		for (Object s : list) {
			result.add(s.toString());
		}
		return result;

	}

	public LogEntryResult getLogs(Map<String, String> args) {
		List<String> stringArgsList = new ArrayList<String>();
		stringArgsList.add(args.get("username"));
		stringArgsList.add(args.get("page"));
		String logsUri = getLogsUri();
		for (String param : Arrays.asList("timestart", "timeend", "tags",
				"applicationId"))
			if (args.containsKey(param)) {
				logsUri = logsUri.concat("&" + param + "={" + param + "}");
				stringArgsList.add(args.get(param));
			}
		return get(logsUri, LogEntryResult.class, stringArgsList.toArray());
	}

	public ProblemDefinitionIndex getProblemDefinitions(int userId) {
		return get(getProblemDefinitionsUri(), ProblemDefinitionIndex.class, userId);
	}

	public void createProfile(int userId, String language) {
		get(getProfileCreateUri(), Void.class, userId, language);
	}
}
