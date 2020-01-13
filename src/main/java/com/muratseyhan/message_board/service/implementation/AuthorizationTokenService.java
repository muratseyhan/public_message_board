package com.muratseyhan.message_board.service.implementation;

import com.muratseyhan.message_board.error.AuthorizationError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizationTokenService implements com.muratseyhan.message_board.service.AuthorizationTokenService {
	final static String BEARER = "Bearer ";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${authorization.token.validity:3600000}") // Defaults to one hour
	private long tokenValidity;

	@Override
	public String generateToken(final UserDetails userDetails) {
		final long issuedAtMillis = System.currentTimeMillis();

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(issuedAtMillis))
				.setExpiration(new Date(issuedAtMillis + tokenValidity))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	/**
	 * Extracts username from the JWT token provided in <code>authorizationHeader</code>
	 *
	 * @param authorizationHeader authorization header value
	 * @return username that the token belongs to
	 * @throws AuthorizationError if the token is expired or could not be parsed
	 **/
	public String extractUserNameFromAuthorizationHeader(final String authorizationHeader) throws AuthorizationError {
		return extractTokenFromAuthorizationHeader(authorizationHeader)
				.map(this::extractUsername)
				.orElseThrow(() -> new AuthorizationError("Malformed Authorization header"));
	}

	private Optional<String> extractTokenFromAuthorizationHeader(final String authorizationHeader) throws AuthorizationError {
		if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
			return Optional.of(authorizationHeader.substring(7));
		}

		return Optional.empty();
	}

	private String extractUsername(final String token) throws AuthorizationError {
		String username = extractClaims(token).getSubject();

		if (username == null) {
			throw new AuthorizationError(String.format("JWT token contains no subject claim: %s", token));
		}

		return username;
	}

	/**
	 * Parses a JWT Claims set from given JWT token
	 *
	 * @param token a JWT token
	 * @return a JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4">Claims set</a>
	 * @throws AuthorizationError if the token is expired or could not be parsed
	 **/
	private Claims extractClaims(final String token) throws AuthorizationError {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (JwtException | IllegalArgumentException ex) {
			throw new AuthorizationError("Invalid token", ex);
		}
	}
}
