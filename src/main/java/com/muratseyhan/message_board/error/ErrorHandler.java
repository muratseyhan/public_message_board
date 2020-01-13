package com.muratseyhan.message_board.error;

import com.muratseyhan.message_board.error.implementation.ErrorResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
	private static final String FIELD_NAME_DELIMITER = ", ";

	@Autowired
	ErrorResponseEntityFactory errorResponseEntityFactory;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();

		final String invalidFields = result.getFieldErrors().stream()
				.map(FieldError::getField)
				.collect(Collectors.joining(FIELD_NAME_DELIMITER));

		return errorResponseEntityFactory.createValidationErrorResponseEntity(invalidFields);
	}

	@ExceptionHandler(AuthorizationError.class)
	@ResponseBody
	public ResponseEntity<?> handleAuthenticationError() {
		return errorResponseEntityFactory.createUnauthorizedResponseEntity();
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	public ResponseEntity<?> handleAuthenticationError(AuthenticationException ex) {
		return errorResponseEntityFactory.createAuthenticationErrorResponseEntity(ex.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<?> handleMethodNotSupportedError(HttpRequestMethodNotSupportedException ex) {
		return errorResponseEntityFactory.createMethodNotSupportedErrorResponseEntity(ex.getMessage());
	}

	@ExceptionHandler({
			TypeMismatchException.class,
			BindException.class,
			HttpMessageNotReadableException.class,
			MissingServletRequestParameterException.class,
			MethodArgumentTypeMismatchException.class
	})
	@ResponseBody
	public ResponseEntity<?> handleBadRequest(Exception ex) {
		return errorResponseEntityFactory.createBadRequestResponseEntity();
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<?> handleRest(Exception ex) {
		return errorResponseEntityFactory.createInternalServerErrorResponseEntity();
	}
}
