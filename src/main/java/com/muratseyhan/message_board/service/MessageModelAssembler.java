package com.muratseyhan.message_board.service;

import com.muratseyhan.message_board.data.entity.MessageEntity;
import com.muratseyhan.message_board.model.MessageModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface MessageModelAssembler extends RepresentationModelAssembler<MessageEntity, MessageModel> {
	MessageModel toModel(MessageEntity messageEntity);

	MessageModel instantiateModel(MessageEntity messageEntity);
}
