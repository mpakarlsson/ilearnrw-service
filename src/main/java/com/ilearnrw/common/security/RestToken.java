package com.ilearnrw.common.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.token.Token;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;


public class RestToken extends UsernamePasswordAuthenticationToken {

	
    public RestToken(String key, String credentials) {
        super(key, credentials);
    }

    public RestToken(String key, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(key, credentials, authorities);
    }

    public String getPrincipal() {
        return (String) super.getPrincipal();
    }

    public String getCredentials() {
        return (String) super.getCredentials();
    }

	public static RestToken fromToken(Token receivedToken) {
		String serialized = receivedToken.getExtendedInformation();
		ObjectMapper mapper = new ObjectMapper();
		AuthTokenData data;
		try {
			data = mapper.readValue(serialized, AuthTokenData.class);
			return new RestToken(data.getUserName(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
