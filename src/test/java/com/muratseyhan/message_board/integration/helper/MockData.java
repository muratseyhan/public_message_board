package com.muratseyhan.message_board.integration.helper;

import com.muratseyhan.message_board.model.MessageRequest;
import com.muratseyhan.message_board.model.UserInfoRequest;

public class MockData {
	public final static String VALID_USERNAME = "manfred";
	public final static String VALID_USERNAME_TWO = "manfredTwo";
	public final static String VALID_PASSWORD = "123";
	public final static String VALID_PASSWORD_TWO = "456";
	public final static String VALID_MESSAGE_TITLE = "What a title!";
	public final static String VALID_MESSAGE_TITLE_TWO = "A different title!";
	public final static String VALID_MESSAGE_BODY = "This is my message to you all.";
	public final static String VALID_MESSAGE_BODY_TWO = "A very different message this time.";
	public final static String INVALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJpYXQiOjE1Nzg3MzgzNTgsImV4cCI6MTU3ODc0MTk1OH0.gvJtbzyvBRIN_Bn3xWxoCbglt3TMnVUK3qGCBve0AhBJuGoOt7qGxgqmgpCqPLoELaY7qSNYg4ATySlZWK1xeA";
	public final static String MALFORMED_TOKEN = "Malformed token";

	public final static UserInfoRequest VALID_USER_INFO_REQUEST = new UserInfoRequest(VALID_USERNAME, VALID_PASSWORD);
	public final static UserInfoRequest VALID_USER_INFO_REQUEST_TWO = new UserInfoRequest(VALID_USERNAME_TWO, VALID_PASSWORD_TWO);
	public final static UserInfoRequest USER_INFO_REQUEST_MISSING_USERNAME = new UserInfoRequest(null, VALID_PASSWORD);
	public final static UserInfoRequest USER_INFO_REQUEST_MISSING_PASSWORD = new UserInfoRequest(VALID_USERNAME, null);

	public final static MessageRequest VALID_MESSAGE_REQUEST = new MessageRequest(VALID_MESSAGE_TITLE, VALID_MESSAGE_BODY);
	public final static MessageRequest VALID_MESSAGE_REQUEST_TWO = new MessageRequest(VALID_MESSAGE_TITLE_TWO, VALID_MESSAGE_BODY_TWO);
	public final static MessageRequest MESSAGE_REQUEST_MISSING_TITLE = new MessageRequest(null, VALID_MESSAGE_BODY);
	public final static MessageRequest MESSAGE_REQUEST_MISSING_BODY = new MessageRequest(VALID_MESSAGE_TITLE, null);
}
