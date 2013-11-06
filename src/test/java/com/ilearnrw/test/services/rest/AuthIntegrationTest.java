package com.ilearnrw.test.services.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import ilearnrw.user.UserProfile;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilearnrw.services.profileAccessUpdater.DbProfileProvider;
import com.ilearnrw.services.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.services.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.services.rest.AuthController;
import com.ilearnrw.services.security.RefreshTokenData;
import com.ilearnrw.services.security.Tokens;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring/test-context.xml"})
public class AuthIntegrationTest {
	MockMvc mockMvc;

	@InjectMocks
	AuthController controller;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = standaloneSetup(controller).setMessageConverters(
				new MappingJackson2HttpMessageConverter(),
				new Jaxb2RootElementHttpMessageConverter()).build();
	}

	@Test
	public void thatAuthUsesHttpOK() throws Exception {

		this.mockMvc
				.perform(
						get("/user/auth").param("username", "admin")
								.param("pass", "admin")
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void thatAuthHasRequiredParams() throws Exception {

		this.mockMvc.perform(
				get("/user/auth").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		this.mockMvc
				.perform(
						get("/user/auth").param("username", "").accept(
								MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void thatRefreshTokensCanBeDecrypted() throws Exception {

		String content = this.mockMvc
				.perform(
						get("/user/auth").accept(MediaType.APPLICATION_JSON)
								.param("username", "test_user")
								.param("pass", "test_password")).andDo(print())
				.andExpect(jsonPath("$.auth").exists())
				.andExpect(jsonPath("$.refresh").exists()).andReturn()
				.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		Tokens tokens = mapper.readValue(content, Tokens.class);
		
		Assert.assertNotNull(tokens);
		
		RefreshTokenData upp = mapper.readValue(tokens.getRefreshToken(), RefreshTokenData.class);

		Assert.assertNotNull(upp);

		Assert.assertEquals("test_user", upp.getUserName());
		Assert.assertEquals("test_password", upp.getPassword());
	}

	/*
	 * @Test public void thatViewOrderRendersXMLCorrectly() throws Exception {
	 * 
	 * this.mockMvc.perform( get("/user/auth") .accept(MediaType.TEXT_XML))
	 * .andDo(print()) .andExpect(content().contentType(MediaType.TEXT_XML));
	 * //.andExpect(xpath("/auth").string(key.toString())); }
	 */

	static HttpHeaders getHeaders(String auth) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
		headers.add("Authorization", "Basic "
				+ new String(encodedAuthorisation));

		return headers;
	}
	
	@Autowired
	IProfileProvider profileProvider;
	
	@Test
	public void testUserProfileSerialization() throws ProfileProviderException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		UserProfile profile = profileProvider.getProfile("1");
		String s = objectMapper.writeValueAsString(profile);
		
		UserProfile profileBack = objectMapper.readValue(s, UserProfile.class);
		Assert.assertNotNull(profileBack);
		//Assert.assertEquals(profile, profileBack);
	}
}
