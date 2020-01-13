package com.muratseyhan.message_board.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratseyhan.message_board.data.entity.MessageEntity;
import com.muratseyhan.message_board.data.repository.MessageRepository;
import com.muratseyhan.message_board.integration.helper.AuthenticationTestHelper;
import com.muratseyhan.message_board.integration.helper.MessageTestHelper;
import com.muratseyhan.message_board.integration.helper.ResponseVerifier;
import com.muratseyhan.message_board.integration.helper.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.muratseyhan.message_board.integration.helper.MockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class MessageTests {
	final static String LOCATION_HEADER_PATTERN = "**/messages/%s";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserTestHelper userTestHelper;

	@Autowired
	private MessageTestHelper messageTestHelper;

	@Autowired
	private AuthenticationTestHelper authenticationTestHelper;

	@Autowired
	private ResponseVerifier responseVerifier;

	@Test
	void createMessage_whenUserIsAuthorized_saveMessageAndReturnCreated() throws Exception {
		// Setup
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final String token = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST);

		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST, token);

		// Verify
		final List<MessageEntity> messageEntities = messageRepository.findAll();
		assertEquals(1, messageEntities.size());

		final MessageEntity messageEntity = messageEntities.get(0);
		assertEquals(VALID_USER_INFO_REQUEST.getUsername(), messageEntity.getAuthor().getUsername());
		assertEquals(VALID_MESSAGE_REQUEST.getTitle(), messageEntity.getTitle());
		assertEquals(VALID_MESSAGE_REQUEST.getBody(), messageEntity.getBody());

		result.andExpect(redirectedUrlPattern(String.format(LOCATION_HEADER_PATTERN, messageEntity.getId())));
	}

	@Test
	void createMessage_whenTokenIsInvalid_returnUnauthorized() throws Exception {
		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST, INVALID_TOKEN);

		// Verify
		responseVerifier.verifyUnauthorized(result);
	}

	@Test
	void createMessage_whenTokenIsMalformed_returnUnauthorized() throws Exception {
		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST, MALFORMED_TOKEN);

		// Verify
		responseVerifier.verifyUnauthorized(result);
	}

	@Test
	void createMessage_whenAuthorizationHeaderIsMissing_returnUnauthorized() throws Exception {
		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST);

		// Verify
		responseVerifier.verifyUnauthorized(result);
	}

	@Test
	void createMessage_whenTitleIsMissing_returnBadRequest() throws Exception {
		// Setup
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final String token = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST);

		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(MESSAGE_REQUEST_MISSING_TITLE, token);

		// Verify
		responseVerifier.verifyBadRequest(result);

		final List<MessageEntity> messageEntities = messageRepository.findAll();
		assertEquals(0, messageEntities.size());
	}

	@Test
	void createMessage_whenBodyIsMissing_returnBadRequest() throws Exception {
		// Setup
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final String token = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST);

		// Execute
		final ResultActions result = messageTestHelper.attemptToCreate(MESSAGE_REQUEST_MISSING_BODY, token);

		// Verify
		responseVerifier.verifyBadRequest(result);

		final List<MessageEntity> messageEntities = messageRepository.findAll();
		assertEquals(0, messageEntities.size());
	}

	@Test
	void updateMessage_whenUserIsAuthorizedAndAuthor_updateMessageAndReturnNoContent() throws Exception {
		// Setup
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final String token = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST);
		final ResultActions resultActions = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST, token);
		final String resourceUri = resultActions.andReturn().getResponse().getRedirectedUrl();

		// Execute
		final ResultActions result = messageTestHelper.attemptToUpdate(resourceUri, VALID_MESSAGE_REQUEST_TWO, token);

		// Verify
		result.andExpect(status().isNoContent());

		final List<MessageEntity> messageEntities = messageRepository.findAll();
		assertEquals(1, messageEntities.size());

		final MessageEntity messageEntity = messageEntities.get(0);
		assertEquals(VALID_USER_INFO_REQUEST.getUsername(), messageEntity.getAuthor().getUsername());
		assertEquals(VALID_MESSAGE_REQUEST_TWO.getTitle(), messageEntity.getTitle());
		assertEquals(VALID_MESSAGE_REQUEST_TWO.getBody(), messageEntity.getBody());
	}

	@Test
	void updateMessage_whenUserIsAuthorizedAndNotAuthor_returnUnauthorized() throws Exception {
		// Setup
		// Create a user and a message
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST);
		final String token = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST);
		final ResultActions resultActions = messageTestHelper.attemptToCreate(VALID_MESSAGE_REQUEST, token);
		final String resourceUri = resultActions.andReturn().getResponse().getRedirectedUrl();
		// Create user two
		userTestHelper.attemptToCreateUser(VALID_USER_INFO_REQUEST_TWO);
		final String tokenTwo = authenticationTestHelper.attemptToAuthenticateAndExtractToken(VALID_USER_INFO_REQUEST_TWO);

		// Execute
		// Attempt to update the message as user two
		final ResultActions result = messageTestHelper.attemptToUpdate(resourceUri, VALID_MESSAGE_REQUEST_TWO, tokenTwo);

		// Verify
		responseVerifier.verifyUnauthorized(result);

		final List<MessageEntity> messageEntities = messageRepository.findAll();
		assertEquals(1, messageEntities.size());

		final MessageEntity messageEntity = messageEntities.get(0);
		assertEquals(VALID_USER_INFO_REQUEST.getUsername(), messageEntity.getAuthor().getUsername());
		assertEquals(VALID_MESSAGE_REQUEST.getTitle(), messageEntity.getTitle());
		assertEquals(VALID_MESSAGE_REQUEST.getBody(), messageEntity.getBody());
	}

	// TODO - test getMessage, deleteMessage
}
