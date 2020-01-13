package com.muratseyhan.message_board.service;

import com.muratseyhan.message_board.model.MessageModel;
import com.muratseyhan.message_board.model.MessageRequest;
import org.springframework.hateoas.CollectionModel;

import java.util.Optional;

public interface MessageService {
	CollectionModel<MessageModel> findAllMessages();

	MessageModel findMessage(Long id);

	MessageModel createMessage(MessageRequest messageRequest, String username);

	boolean updateMessage(Long id, MessageRequest messageRequest, String username);

	boolean deleteMessage(Long id, String username);
}
