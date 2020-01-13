package com.muratseyhan.message_board.error;

public class AuthorizationError extends RuntimeException {
	private static final long serialVersionUID = -190113820670439902L;

	public AuthorizationError(String msg) {
		super(msg);
	}

	public AuthorizationError(String msg, Throwable t) {
		super(msg, t);
	}
}
