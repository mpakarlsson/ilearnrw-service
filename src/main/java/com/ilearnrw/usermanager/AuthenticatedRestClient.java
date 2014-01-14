package com.ilearnrw.usermanager;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.ilearnrw.services.datalogger.model.LogEntryResult;
import com.ilearnrw.services.security.Tokens;
import com.ilearnrw.usermanager.model.User;

@Component
public class AuthenticatedRestClient {
	private RestTemplate template;
	private HttpHeaders headers;

	@Value("${auth.baseurl}")
	private String authBaseUri;

	@Value("${logs.baseurl}")
	private String logsBaseUri;

	private String rolesUri = "user/roles?token={token}";
	private String userDetailsUri = "user/details/{username}";
	private String authUri = "user/auth?username={username}&pass={pass}";
	private String logsUri = "logs/{username}?page={page}";

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

		this.template = new RestTemplate();
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

	public String getRolesUri() {
		return authBaseUri + rolesUri;
	}

	public String getAuthUri() {
		return authBaseUri + authUri;
	}

	public String getLogsUri() {
		return logsBaseUri + logsUri;
	}

	public User getUserDetails(String username) {
		return get(getUserDetailsUri(), User.class, username);
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
}
