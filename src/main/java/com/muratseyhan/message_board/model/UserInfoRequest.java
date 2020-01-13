package com.muratseyhan.message_board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserInfoRequest {
	@NotNull
	private final String username;
	@NotNull
	private final String password;
}
