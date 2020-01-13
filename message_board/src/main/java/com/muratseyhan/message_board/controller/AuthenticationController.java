package com.muratseyhan.message_board.controller;

import com.muratseyhan.message_board.model.UserInfoRequest;
import com.muratseyhan.message_board.model.AuthenticationToken;
import com.muratseyhan.message_board.service.AuthorizationTokenService;
import com.muratseyhan.message_board.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import javax.validation.Valid;

@RestController
@RequestMapping("authentication")
@CrossOrigin
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthorizationTokenService authorizationTokenService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@PostMapping
	public ResponseEntity<AuthenticationToken> createAuthenticationToken(@Valid @RequestBody UserInfoRequest userInfoRequest) throws AuthenticationException {
		authenticate(userInfoRequest.getUsername(), userInfoRequest.getPassword());

		final UserDetails userDetails = customUserDetailsService
				.loadUserByUsername(userInfoRequest.getUsername());

		final String token = authorizationTokenService.generateToken(userDetails);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AuthenticationToken(token));
	}

	private void authenticate(String username, String password) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
