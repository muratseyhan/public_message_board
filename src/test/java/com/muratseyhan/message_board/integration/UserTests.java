package com.muratseyhan.message_board.integration;

import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import com.muratseyhan.message_board.integration.helper.ResponseVerifier;
import com.muratseyhan.message_board.integration.helper.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import static com.muratseyhan.message_board.integration.helper.MockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserTests {
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private UserTestHelper userTestHelper;

	@Autowired
	private ResponseVerifier responseVerifier;

	@Test
	void createUser_whenUserInfoRequestIsValid_returnCreatedAndSaveUserInfo() throws Exception {
		// Execute
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST)
				.andExpect(content().string(""))
				.andExpect(status().isCreated());

		// Verify
		final UserInfoEntity userInfoEntity = userInfoRepository.findByUsername(VALID_USER_INFO_REQUEST.getUsername());
		assertEquals(userInfoEntity.getUsername(), VALID_USER_INFO_REQUEST.getUsername());
		assertNotEquals(userInfoEntity.getPassword(), VALID_USER_INFO_REQUEST.getPassword());
	}

	@Test
	void createUser_whenUserCreationAttemptedTwice_saveOneUserAndReturnBadRequestOnSecond() throws Exception {
		// Setup
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);

		// Execute
		final ResultActions result = userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);

		// Verify
		responseVerifier.verifyBadRequest(result);
		final Long userInfoCount = userInfoRepository.count();
		assertEquals(1, userInfoCount);
	}

	@Test
	void createUser_whenUsernameIsMissing_returnBadRequestAndDoNotSaveUser() throws Exception {
		// Execute
		final ResultActions result = userTestHelper.attemptToCreateUser(USER_INFO_REQUEST_MISSING_USERNAME);

		// Verify
		responseVerifier.verifyBadRequest(result);
		final Long userInfoCount = userInfoRepository.count();
		assertEquals(0, userInfoCount);
	}

	@Test
	void createUser_whenPasswordIsMissing_returnBadRequestAndDoNotSaveUser() throws Exception {
		// Execute
		final ResultActions result = userTestHelper.attemptToCreateUser(USER_INFO_REQUEST_MISSING_PASSWORD);

		// Verify
		responseVerifier.verifyBadRequest(result);
		final Long userInfoCount = userInfoRepository.count();
		assertEquals(0, userInfoCount);
	}
}
