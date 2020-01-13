package com.muratseyhan.message_board.unit.controller;

import com.muratseyhan.message_board.controller.MessageController;
import com.muratseyhan.message_board.error.ErrorResponseEntityFactory;
import com.muratseyhan.message_board.model.MessageModel;
import com.muratseyhan.message_board.model.MessageRequest;
import com.muratseyhan.message_board.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTests {
	private static final URI MESSAGE_URI = URI.create("http://example.org");
	private static final String AUTHENTICATION_NAME = "Name";
	private static final Long MESSAGE_ID = 123L;

	@InjectMocks
	private MessageController messageController;

	@Mock
	private MessageService messageService;

	@Mock
	private ErrorResponseEntityFactory errorResponseEntityFactory;

	@Mock
	private ResponseEntity<VndErrors.VndError> messageNotFoundResponseEntity;

	@Mock
	private ResponseEntity<VndErrors.VndError> unauthorizedResponseEntity;

	@Mock
	private MessageRequest messageRequest;

	@Mock
	private Authentication authentication;

	@Mock
	private MessageModel messageModel;

	@Mock
	private Link messageLink;

	@Mock
	private CollectionModel<MessageModel> messageModels;

	@Test
	public void findAll_returnOkResponseEntityOfMessageModels() {
		// Setup
		when(messageService.findAllMessages()).thenReturn(messageModels);

		// Execute
		HttpEntity<CollectionModel<MessageModel>> result = messageController.findAll();

		// Verify
		assertEquals(result, ResponseEntity.ok(messageModels));
	}

	@Test
	public void create_createMessageAndReturnsCreated() {
		// Setup
		when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
		when(messageService.createMessage(messageRequest, AUTHENTICATION_NAME)).thenReturn(messageModel);
		when(messageModel.getRequiredLink("self")).thenReturn(messageLink);
		when(messageLink.toUri()).thenReturn(MESSAGE_URI);

		// Execute
		HttpEntity<MessageModel> result = messageController.create(messageRequest, authentication);

		// Verify
		assertEquals(ResponseEntity.created(MESSAGE_URI).build(), result);
	}

	@Test
	public void findOne_whenMessageIsPresent_returnMessageResponseEntity() {
		// Setup
		when(messageService.findMessage(MESSAGE_ID)).thenReturn(messageModel);

		// Execute
		HttpEntity<?> result = messageController.findOne(MESSAGE_ID);

		// Verify
		assertEquals(ResponseEntity.ok(messageModel), result);
	}

	@Test
	public void findOne_whenMessageIsMissing_returnMessageNotFoundResponseEntity() {
		// Setup
		when(messageService.findMessage(MESSAGE_ID)).thenReturn(null);
		when(errorResponseEntityFactory.createMessageNotFoundResponseEntity(MESSAGE_ID)).thenReturn(messageNotFoundResponseEntity);

		// Execute
		HttpEntity<?> result = messageController.findOne(MESSAGE_ID);

		// Verify
		assertEquals(messageNotFoundResponseEntity, result);
	}

	@Test
	public void update_whenUpdateSucceeds_returnNoContentResponseEntity() {
		// Setup
		when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
		when(messageService.updateMessage(MESSAGE_ID, messageRequest, AUTHENTICATION_NAME)).thenReturn(true);

		// Execute
		HttpEntity<?> result = messageController.update(MESSAGE_ID, messageRequest, authentication);

		// Verify
		assertEquals(ResponseEntity.noContent().build(), result);
	}

	@Test
	public void update_whenUpdateFails_returnUnauthorizedResponseEntity() {
		// Setup
		when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
		when(messageService.updateMessage(MESSAGE_ID, messageRequest, AUTHENTICATION_NAME)).thenReturn(false);
		when(errorResponseEntityFactory.createUnauthorizedResponseEntity()).thenReturn(unauthorizedResponseEntity);

		// Execute
		HttpEntity<?> result = messageController.update(MESSAGE_ID, messageRequest, authentication);

		// Verify
		assertEquals(unauthorizedResponseEntity, result);
	}

	@Test
	public void delete_whenDeletionSucceeds_returnNoContentResponseEntity() {
		// Setup
		when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
		when(messageService.deleteMessage(MESSAGE_ID, AUTHENTICATION_NAME)).thenReturn(true);

		// Execute
		HttpEntity<?> result = messageController.delete(MESSAGE_ID, authentication);

		// Verify
		assertEquals(ResponseEntity.noContent().build(), result);
	}

	@Test
	public void update_whenDeletionFails_returnUnauthorizedResponseEntity() {
		// Setup
		when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
		when(messageService.deleteMessage(MESSAGE_ID, AUTHENTICATION_NAME)).thenReturn(false);
		when(errorResponseEntityFactory.createUnauthorizedResponseEntity()).thenReturn(unauthorizedResponseEntity);

		// Execute
		HttpEntity<?> result = messageController.delete(MESSAGE_ID, authentication);

		// Verify
		assertEquals(unauthorizedResponseEntity, result);
	}
}