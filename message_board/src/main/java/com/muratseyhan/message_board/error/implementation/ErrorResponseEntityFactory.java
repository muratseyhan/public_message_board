package com.muratseyhan.message_board.error.implementation;

import com.muratseyhan.message_board.error.ErrorLogRef;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseEntityFactory implements com.muratseyhan.message_board.error.ErrorResponseEntityFactory {
	private static final String ERROR_CONTENT_TYPE = "application/vnd.error+json";
	private static final String UNAUTHORIZED_MESSAGE = "The user is not authorized to perform this action.";
	private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
	private static final String BAD_REQUEST_MESSAGE = "Bad Request";
	private static final String NOT_FOUND_MESSAGE_TEMPLATE = "Could not find a message with id %s";
	private static final String VALIDATION_ERROR_MESSAGE_TEMPLATE = "The following fields have invalid values: %s";
	private static final String USERNAME_TAKEN_MESSAGE_TEMPLATE = "Username '%s' is taken";

	@Override
	public ResponseEntity<VndErrors.VndError> createUnauthorizedResponseEntity() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.UNAUTHORIZED.getName(), UNAUTHORIZED_MESSAGE));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createAuthenticationErrorResponseEntity(final String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.UNAUTHORIZED.getName(), message));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createMethodNotSupportedErrorResponseEntity(final String message) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.METHOD_NOT_SUPPORTED.getName(), message));
	}

	public ResponseEntity<VndErrors.VndError> createInternalServerErrorResponseEntity() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.INTERNAL_SERVER_ERROR.getName(), INTERNAL_SERVER_ERROR_MESSAGE));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createMessageNotFoundResponseEntity(final Long id) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.NOT_FOUND.getName(), String.format(NOT_FOUND_MESSAGE_TEMPLATE, id)));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createValidationErrorResponseEntity(final String fields) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.BAD_REQUEST.getName(), String.format(VALIDATION_ERROR_MESSAGE_TEMPLATE, fields)));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createUsernameTakenResponseEntity(final String username) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.BAD_REQUEST.getName(), String.format(USERNAME_TAKEN_MESSAGE_TEMPLATE, username)));
	}

	@Override
	public ResponseEntity<VndErrors.VndError> createBadRequestResponseEntity() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header(HttpHeaders.CONTENT_TYPE, ERROR_CONTENT_TYPE)
				.body(new VndErrors.VndError(ErrorLogRef.BAD_REQUEST.getName(), BAD_REQUEST_MESSAGE));
	}
}
