package com.muratseyhan.message_board.integration.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratseyhan.message_board.model.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Component
public class MessageTestHelper {
	private static final String MESSAGES_PATH = "/messages";
	private static final String AUTHORIZATION_HEADER_TEMPLATE = "Bearer %s";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public ResultActions attemptToCreate(final MessageRequest messageRequest)
			throws Exception {
		return mockMvc.perform(createCreateRequestWithoutAuthorizationHeader(messageRequest));
	}

	public ResultActions attemptToCreate(final MessageRequest messageRequest, final String token)
			throws Exception {
		final String authorizationHeader = String.format(AUTHORIZATION_HEADER_TEMPLATE, token);

		return mockMvc.perform(createCreateRequestWithoutAuthorizationHeader(messageRequest)
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader));
	}

	private MockHttpServletRequestBuilder createCreateRequestWithoutAuthorizationHeader(final MessageRequest messageRequest) throws JsonProcessingException {
		return post(MESSAGES_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(messageRequest));
	}

	public ResultActions attemptToUpdate(final String resourceUri, final MessageRequest messageRequest)
			throws Exception {
		return mockMvc.perform(createUpdateRequestWithoutAuthorizationHeader(resourceUri, messageRequest));
	}

	public ResultActions attemptToUpdate(final String resourceUri, final MessageRequest messageRequest, final String token)
			throws Exception {
		final String authorizationHeader = String.format(AUTHORIZATION_HEADER_TEMPLATE, token);

		return mockMvc.perform(createUpdateRequestWithoutAuthorizationHeader(resourceUri, messageRequest)
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader));
	}

	private MockHttpServletRequestBuilder createUpdateRequestWithoutAuthorizationHeader(final String resourceUri, final MessageRequest messageRequest) throws JsonProcessingException {
		return put(resourceUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(messageRequest));
	}
}
