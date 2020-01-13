package com.muratseyhan.message_board.integration.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratseyhan.message_board.model.UserInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class UserTestHelper {
	private static final String USERS_PATH = "/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public ResultActions attemptToCreateUser(final UserInfoRequest userInfoRequest)
			throws Exception {
		return mockMvc.perform(post(USERS_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userInfoRequest)));
	}
}
