package com.muratseyhan.message_board.unit.controller;

import com.muratseyhan.message_board.controller.AuthenticationController;
import com.muratseyhan.message_board.model.AuthenticationToken;
import com.muratseyhan.message_board.model.UserInfoRequest;
import com.muratseyhan.message_board.service.AuthorizationTokenService;
import com.muratseyhan.message_board.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTests {
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String TOKEN = "token";

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private AuthorizationTokenService authorizationTokenService;

	@Mock
	private CustomUserDetailsService customUserDetailsService;

	@Mock
	private UserInfoRequest userInfoRequest;

	@Mock
	private UserDetails userDetails;

	@BeforeEach
	public void setup() {
		when(userInfoRequest.getUsername()).thenReturn(USERNAME);
	}

	@Test
	public void createAuthenticationToken_authenticateUserAndReturnAuthorizationToken() {
		// Setup
		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
				= new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

		when(userInfoRequest.getUsername()).thenReturn(USERNAME);
		when(userInfoRequest.getPassword()).thenReturn(PASSWORD);
		when(customUserDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
		when(authorizationTokenService.generateToken(userDetails)).thenReturn(TOKEN);

		// Execute
		final ResponseEntity<AuthenticationToken> result =  authenticationController.createAuthenticationToken(userInfoRequest);

		// Verify
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(TOKEN, result.getBody().getToken());
		verify(authenticationManager, times(1)).authenticate(usernamePasswordAuthenticationToken);
	}
}