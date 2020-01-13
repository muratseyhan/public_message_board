package com.muratseyhan.message_board.config;

import com.muratseyhan.message_board.error.AuthorizationError;
import com.muratseyhan.message_board.service.AuthorizationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {
	@Autowired
	private AuthorizationTokenService authorizationTokenService;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * If an Authorization header is present and contains a valid JWT token, loads
	 * user details associated with the token. If the user is loaded successfully,
	 * sets the user details into the Spring SecurityContext.
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(AUTHORIZATION);

		if (authorizationHeader != null && !authorizationHeader.isBlank()) {
			try {
				final String username = authorizationTokenService.extractUserNameFromAuthorizationHeader(authorizationHeader);
				loadUserAndSetAuthentication(username, request);

				log.debug(String.format("Security context set for user '%s' on '%s' '%s'", username, request.getContextPath(), request.getMethod()));
			} catch (AuthorizationError ex) {
				log.warn(ex.getMessage());
			}
		}

		chain.doFilter(request, response);
	}

	private void loadUserAndSetAuthentication(final String username, final HttpServletRequest request) throws AuthorizationError {
		try {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

			usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		} catch (UsernameNotFoundException ex) {
			throw new AuthorizationError(String.format("Could not find user '%s'", username), ex);
		}
	}
}
