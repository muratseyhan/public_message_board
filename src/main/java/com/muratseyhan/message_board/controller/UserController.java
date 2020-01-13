package com.muratseyhan.message_board.controller;

import com.muratseyhan.message_board.error.ErrorResponseEntityFactory;
import com.muratseyhan.message_board.model.UserInfoRequest;
import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ErrorResponseEntityFactory errorResponseEntityFactory;

	@PostMapping
	public HttpEntity<?> create(@Valid @RequestBody UserInfoRequest userInfoRequest) {
		String username = userInfoRequest.getUsername();

		if (userInfoRepository.existsByUsername(username)) {
			return errorResponseEntityFactory.createUsernameTakenResponseEntity(username);
		}

		String password = userInfoRequest.getPassword();
		String encodedPassword = passwordEncoder.encode(password);

		userInfoRepository.save(new UserInfoEntity(username, encodedPassword));

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
