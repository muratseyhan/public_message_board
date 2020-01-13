package com.muratseyhan.message_board.unit.controller;

import com.muratseyhan.message_board.controller.UserController;
import com.muratseyhan.message_board.error.ErrorResponseEntityFactory;
import com.muratseyhan.message_board.model.UserInfoRequest;
import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String ENCODED_PASSWORD = "encoded password";

	@InjectMocks
	private UserController userController;

	@Mock
	private UserInfoRepository userInfoRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ErrorResponseEntityFactory errorResponseEntityFactory;

	@Mock
	private ResponseEntity<VndErrors.VndError> errorResponseEntity;

	@Mock
	private UserInfoRequest userInfoRequest;

	@BeforeEach
	public void setup() {
		when(userInfoRequest.getUsername()).thenReturn(USERNAME);
	}

	@Test
	public void create_whenUsernameIsTaken_returnUsernameTakenResponseEntityAndDoNotSaveUser() {
		// Setup
		when(userInfoRepository.existsByUsername(USERNAME)).thenReturn(true);
		when(errorResponseEntityFactory.createUsernameTakenResponseEntity(USERNAME)).thenReturn(errorResponseEntity);

		// Execute
		HttpEntity<?> result = userController.create(userInfoRequest);

		// Verify
		assertEquals(result, errorResponseEntity);
		verify(userInfoRepository, times(0)).save(any());
	}

	@Test
	public void create_whenUsernameIsNotTaken_returnCreatedResponseEntityAndSaveUser() {
		// Setup
		final UserInfoEntity userInfoEntity = new UserInfoEntity(USERNAME, ENCODED_PASSWORD);

		when(userInfoRequest.getPassword()).thenReturn(PASSWORD);
		when(userInfoRepository.existsByUsername(USERNAME)).thenReturn(false);
		when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

		// Execute
		HttpEntity<?> result = userController.create(userInfoRequest);

		// Verify
		assertEquals(result, ResponseEntity.status(HttpStatus.CREATED).build());
		verify(userInfoRepository, times(1)).save(userInfoEntity);
	}
}