package com.muratseyhan.message_board.service;

import com.muratseyhan.message_board.error.AuthorizationError;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizationTokenService {
	String generateToken(UserDetails userDetails);

	public String extractUserNameFromAuthorizationHeader(final String authorizationHeader) throws AuthorizationError;
}
