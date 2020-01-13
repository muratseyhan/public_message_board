package com.muratseyhan.message_board.unit.service;

import com.muratseyhan.message_board.data.entity.MessageEntity;
import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.MessageRepository;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import com.muratseyhan.message_board.model.MessageModel;
import com.muratseyhan.message_board.model.MessageRequest;
import com.muratseyhan.message_board.service.implementation.MessageService;
import com.muratseyhan.message_board.service.implementation.MessageModelAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {
	private static final Long MESSAGE_ID = 123L;
	private static final String MESSAGE_TITLE = "message title";
	private static final String MESSAGE_BODY = "message body";
	private static final String USERNAME = "username";

	@InjectMocks
	private MessageService messageService;

	@Mock
	private UserInfoRepository userInfoRepository;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private MessageModelAssembler messageModelAssembler;

	@Mock
	private UserInfoEntity userInfoEntity;

	@Mock
	private MessageEntity messageEntity;

	@Mock
	private MessageEntity updatedMessageEntity;

	@Mock
	private MessageModel messageModel;

	@Mock
	private List<MessageEntity> messageEntities;

	@Mock
	private CollectionModel<MessageModel> messageCollectionModel;

	@Mock
	private MessageRequest messageRequest;

	@Test
	public void findAllMessages_fetchAllMessagesAndReturnCollectionModel() {
		// Setup
		when(messageRepository.findAll()).thenReturn(messageEntities);
		when(messageModelAssembler.toCollectionModel(messageEntities)).thenReturn(messageCollectionModel);

		// Execute
		final CollectionModel<MessageModel> result = messageService.findAllMessages();

		// Verify
		assertEquals(messageCollectionModel, result);
	}

	@Test
	public void findMessage_whenMessageIsPresent_returnMessageModel() {
		// Setup
		when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(messageEntity));
		when(messageModelAssembler.toModel(messageEntity)).thenReturn(messageModel);

		// Execute
		final MessageModel result = messageService.findMessage(MESSAGE_ID);

		// Verify
		assertEquals(messageModel, result);
	}

	@Test
	public void findMessage_whenMessageIsMissing_returnNull() {
		// Setup
		when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.empty());

		// Execute
		final MessageModel result = messageService.findMessage(MESSAGE_ID);

		// Verify
		assertNull(result);
	}

	@Test
	public void createMessage_saveEntityAndReturnModel() {
		// Setup
		when(userInfoRepository.findByUsername(USERNAME)).thenReturn(userInfoEntity);
		when(messageRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);
		when(messageModelAssembler.toModel(messageEntity)).thenReturn(messageModel);
		when(messageRequest.getTitle()).thenReturn(MESSAGE_TITLE);
		when(messageRequest.getBody()).thenReturn(MESSAGE_BODY);

		// Execute
		final MessageModel result = messageService.createMessage(messageRequest, USERNAME);

		// Verify
		assertEquals(messageModel, result);

		final ArgumentCaptor<MessageEntity> messageEntityArgumentCaptor = ArgumentCaptor.forClass(MessageEntity.class);
		verify(messageRepository, times(1)).save(messageEntityArgumentCaptor.capture());
		final MessageEntity capturedEntity = messageEntityArgumentCaptor.getValue();

		assertEquals(MESSAGE_TITLE, capturedEntity.getTitle());
		assertEquals(MESSAGE_BODY, capturedEntity.getBody());
		assertEquals(userInfoEntity, capturedEntity.getAuthor());
	}

	@Test
	public void updateMessage_whenMessageIsPresentForUser_updateMessageAndReturnTrue() {
		// Setup
		when(messageRepository.findByIdAndAuthorUsername(MESSAGE_ID, USERNAME)).thenReturn(Optional.of(messageEntity));
		when(messageRequest.getTitle()).thenReturn(MESSAGE_TITLE);
		when(messageRequest.getBody()).thenReturn(MESSAGE_BODY);
		when(messageRepository.save(any(MessageEntity.class))).thenReturn(updatedMessageEntity);

		// Execute
		final boolean result = messageService.updateMessage(MESSAGE_ID, messageRequest, USERNAME);

		// Verify
		assertTrue(result);
		verify(messageEntity, times(1)).setTitle(MESSAGE_TITLE);
		verify(messageEntity, times(1)).setBody(MESSAGE_BODY);
		verify(messageRepository, times(1)).save(messageEntity);
	}

	@Test
	public void updateMessage_whenMessageIsMissingForUser_doNotUpdateMessageAndReturnFalse() {
		// Setup
		when(messageRepository.findByIdAndAuthorUsername(MESSAGE_ID, USERNAME)).thenReturn(Optional.empty());

		// Execute
		final boolean result = messageService.updateMessage(MESSAGE_ID, messageRequest, USERNAME);

		// Verify
		assertFalse(result);
		verify(messageRepository, times(0)).save(any());
	}

	@Test
	public void deleteMessage_whenMessageIsPresentForUser_deleteMessageAndReturnTrue() {
		// Setup
		when(messageRepository.findByIdAndAuthorUsername(MESSAGE_ID, USERNAME)).thenReturn(Optional.of(messageEntity));

		// Execute
		final boolean result = messageService.deleteMessage(MESSAGE_ID, USERNAME);

		// Verify
		assertTrue(result);
		verify(messageRepository, times(1)).delete(messageEntity);
	}

	@Test
	public void deleteMessage_whenMessageIsMissingForUser_doNotDeleteMessageAndReturnFalse() {
		// Setup
		when(messageRepository.findByIdAndAuthorUsername(MESSAGE_ID, USERNAME)).thenReturn(Optional.empty());

		// Execute
		final boolean result = messageService.deleteMessage(MESSAGE_ID, USERNAME);

		// Verify
		assertFalse(result);
		verify(messageRepository, times(0)).delete(any());
	}
}
