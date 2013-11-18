package com.ilearnrw.usermanager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.ilearnrw.services.datalogger.LogEntryResult;
import com.ilearnrw.services.security.Tokens;

public class AuthenticatedRestClient {
	private RestTemplate template;
	private HttpHeaders headers;
	private String rolesUri = "http://localhost:8080/test/user/roles?token={token}";
	private String authUri = "http://localhost:8080/test/user/auth?username={username}&pass={pass}";
	private String logsUri = "http://localhost:8080/test/logs/{userId}?page={page}";

	public AuthenticatedRestClient() {
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
	
	public Tokens getTokens(String username, String pass)
	{
		return get(authUri, Tokens.class, username, pass);
	}

	public List<String> getRoles(String token){
		List list = this.get(rolesUri, List.class, token);
		List<String> result = new ArrayList<String>();
		for (Object s : list) {
			result.add(s.toString());
		}
		return result;

	}
	
	public LogEntryResult getLogs(Map<String, String> args)
	{
		List<String> stringArgsList = new ArrayList<String>();
		stringArgsList.add(args.get("userId"));
		stringArgsList.add(args.get("page"));
		for (String param : Arrays.asList("timestart", "timeend", "tags", "applicationId", "sessionId"))
			if (args.containsKey(param))
			{
				logsUri = logsUri.concat("&" + param + "={" + param + "}");
				stringArgsList.add(args.get(param));
			}
		return get(logsUri, LogEntryResult.class, stringArgsList.toArray());
	}
}
