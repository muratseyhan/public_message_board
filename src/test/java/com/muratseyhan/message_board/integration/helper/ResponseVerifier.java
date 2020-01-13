package com.muratseyhan.message_board.integration.helper;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Component
public class ResponseVerifier {
	public static final String ERROR_CONTENT_TYPE = "application/vnd.error+json";
	public static final String LOGREF_EXPRESSION = "$.logref";
	public static final String LOGREF_BAD_REQUEST = "Bad Request";
	public static final String LOGREF_UNAUTHORIZED = "Unauthorized";

	public void verifyBadRequest(final ResultActions resultActions) throws Exception {
		resultActions.andExpect(content().contentType(ERROR_CONTENT_TYPE))
				.andExpect(jsonPath(LOGREF_EXPRESSION, is(LOGREF_BAD_REQUEST)))
				.andExpect(status().isBadRequest());
	}

	public void verifyUnauthorized(final ResultActions resultActions) throws Exception {
		resultActions.andExpect(content().contentType(ERROR_CONTENT_TYPE))
				.andExpect(jsonPath(LOGREF_EXPRESSION, is(LOGREF_UNAUTHORIZED)))
				.andExpect(status().isUnauthorized());
	}
}
