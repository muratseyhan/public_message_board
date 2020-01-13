package com.muratseyhan.message_board.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import com.muratseyhan.message_board.integration.helper.AuthenticationTestHelper;
import com.muratseyhan.message_board.integration.helper.ResponseVerifier;
import com.muratseyhan.message_board.integration.helper.UserTestHelper;
import com.muratseyhan.message_board.model.UserInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.muratseyhan.message_board.integration.helper.MockData.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthenticationTests {
	public static final String TOKEN_EXPRESSION = "$.token";
	public static final String TOKEN_REGEX = "^\\S+$";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private UserTestHelper userTestHelper;

	@Autowired
	private AuthenticationTestHelper authenticationTestHelper;

	@Autowired
	private ResponseVerifier responseVerifier;

	@Test
	void createToken_whenUserExistsAndUserInfoRequestIsValid_returnTokenWithCreatedStatus() throws Exception {
		// Execute
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(VALID_USER_INFO_REQUEST);

		// Verify
		result.andExpect(status().isCreated())
				.andExpect(jsonPath(TOKEN_EXPRESSION, matchesRegex(TOKEN_REGEX)));
	}

	@Test
	void createToken_whenUserExistsAndPasswordIsWrong_returnUnauthorized() throws Exception {
		// Setup
		final UserInfoRequest authenticationRequest =
				new UserInfoRequest(VALID_USER_INFO_REQUEST.getUsername(), VALID_PASSWORD_TWO);

		// Execute
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(authenticationRequest);

		// Verify
		responseVerifier.verifyUnauthorized(result);
	}

	@Test
	void createToken_whenUserDoesNotExists_returnUnauthorized() throws Exception {
		// Execute
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(VALID_USER_INFO_REQUEST);

		// Verify
		responseVerifier.verifyUnauthorized(result);
	}

	@Test
	void createToken_whenUsernameIsMissing_returnBadRequest() throws Exception {
		// Execute
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(USER_INFO_REQUEST_MISSING_USERNAME);

		// Verify
		responseVerifier.verifyBadRequest(result);
	}

	@Test
	void createToken_whenPasswordIsMissing_returnBadRequest() throws Exception {
		// Execute
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(USER_INFO_REQUEST_MISSING_USERNAME);

		// Verify
		responseVerifier.verifyBadRequest(result);
	}

	@Test
	void createToken_whenBodyIsEmpty_returnBadRequest() throws Exception {
		// Execute
		final ResultActions result = authenticationTestHelper.attemptToAuthenticate(null);

		// Verify
		responseVerifier.verifyBadRequest(result);
	}
}
