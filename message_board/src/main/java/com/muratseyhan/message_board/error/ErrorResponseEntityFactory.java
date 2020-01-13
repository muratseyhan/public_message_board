package com.muratseyhan.message_board.error;

import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.ResponseEntity;

public interface ErrorResponseEntityFactory {
	ResponseEntity<VndErrors.VndError> createUnauthorizedResponseEntity();

	ResponseEntity<VndErrors.VndError> createAuthenticationErrorResponseEntity(String message);

	ResponseEntity<VndErrors.VndError> createMethodNotSupportedErrorResponseEntity(String message);

	ResponseEntity<VndErrors.VndError> createMessageNotFoundResponseEntity(Long id);

	ResponseEntity<VndErrors.VndError> createValidationErrorResponseEntity(String fields);

	ResponseEntity<VndErrors.VndError> createUsernameTakenResponseEntity(String username);

	ResponseEntity<VndErrors.VndError> createBadRequestResponseEntity();
}
