package com.muratseyhan.message_board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class AuthenticationToken {
	@NotNull
	private final String token;
}
