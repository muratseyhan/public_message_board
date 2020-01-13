package com.muratseyhan.message_board.error;

public enum ErrorLogRef {
	NOT_FOUND("Not Found"),
	UNAUTHORIZED("Unauthorized"),
	METHOD_NOT_SUPPORTED("Method Not Supported"),
	BAD_REQUEST("Bad Request"),
	INTERNAL_SERVER_ERROR("Internal Server Error");

	private final String name;

	ErrorLogRef(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}