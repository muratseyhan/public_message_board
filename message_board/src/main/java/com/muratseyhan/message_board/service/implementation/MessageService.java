package com.muratseyhan.message_board.service.implementation;

import com.muratseyhan.message_board.model.MessageRequest;
import com.muratseyhan.message_board.data.entity.MessageEntity;
import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.MessageRepository;
import com.muratseyhan.message_board.model.MessageModel;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements com.muratseyhan.message_board.service.MessageService {
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MessageModelAssembler messageModelAssembler;

	@Override
	public CollectionModel<MessageModel> findAllMessages() {
		final Iterable<MessageEntity> messageEntities = messageRepository.findAll();

		return messageModelAssembler.toCollectionModel(messageEntities);
	}

	@Override
	public MessageModel findMessage(final Long id) {
		return messageRepository.findById(id)
				.map(messageEntity -> messageModelAssembler.toModel(messageEntity))
				.orElse(null);
	}

	@Override
	public MessageModel createMessage(final MessageRequest messageRequest, final String username) {
		final MessageEntity newMessageEntity = messageRepository.save(toMessageEntity(messageRequest, username));

		return messageModelAssembler.toModel(newMessageEntity);
	}

	private MessageEntity toMessageEntity(final MessageRequest messageRequest, final String username) {
		final UserInfoEntity userInfoEntity = userInfoRepository.findByUsername(username);

		return new MessageEntity()
				.setTitle(messageRequest.getTitle())
				.setBody(messageRequest.getBody())
				.setAuthor(userInfoEntity);
	}

	@Override
	public boolean updateMessage(final Long id, final MessageRequest messageRequest, final String username) {
		return messageRepository.findByIdAndAuthorUsername(id, username)
				.map(messageEntity -> {
					messageEntity.setTitle(messageRequest.getTitle());
					messageEntity.setBody(messageRequest.getBody());
					return messageRepository.save(messageEntity);
				})
				.isPresent();
	}

	@Override
	public boolean deleteMessage(final Long id, final String username) {
		return messageRepository.findByIdAndAuthorUsername(id, username)
				.map(messageEntity -> {
					messageRepository.delete(messageEntity);
					return true;
				})
				.isPresent();
	}
}
