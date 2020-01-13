package com.muratseyhan.message_board.config;

import com.muratseyhan.message_board.error.ErrorResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private ErrorResponseEntityFactory errorResponseEntityFactory;

	/**
	 * In case of an AuthenticationException, return a "401 Unauthorized" response instead of the default "403 Forbidden"
	 */
	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		log.warn(authException.getMessage(), authException);

		final ResponseEntity<VndErrors.VndError> responseEntity = errorResponseEntityFactory.createUnauthorizedResponseEntity();
		final VndErrors.VndError error = responseEntity.getBody();

		if (error != null) {
			response.setStatus(responseEntity.getStatusCodeValue());
			response.setContentType(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).toString());
			response.getWriter().write(String.format("{\"logref\": \"%s\", \"message\": \"%s\"}", error.getLogref(), error.getMessage()));
			response.getWriter().flush();
		}
	}
}
