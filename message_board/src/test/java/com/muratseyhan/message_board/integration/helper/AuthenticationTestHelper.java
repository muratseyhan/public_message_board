package com.muratseyhan.message_board.integration.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratseyhan.message_board.model.UserInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class AuthenticationTestHelper {
	private static final String AUTHENTICATION_PATH = "/authentication";
	private static final String TOKEN_FIELD_NAME = "token";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public String attemptToAuthenticateAndExtractToken(final UserInfoRequest userInfoRequest) throws Exception {
		final ResultActions resultActions = attemptToAuthenticate(userInfoRequest);
		final String responseBody = resultActions.andReturn().getResponse().getContentAsString();

		return (String) new JacksonJsonParser().parseMap(responseBody).get(TOKEN_FIELD_NAME);
	}

	public ResultActions attemptToAuthenticate(final UserInfoRequest userInfoRequest)
			throws Exception {
		return mockMvc.perform(post(AUTHENTICATION_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userInfoRequest)));
	}
}
