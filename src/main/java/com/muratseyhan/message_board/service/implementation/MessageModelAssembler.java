package com.muratseyhan.message_board.service.implementation;

import com.muratseyhan.message_board.data.entity.MessageEntity;
import com.muratseyhan.message_board.controller.MessageController;
import com.muratseyhan.message_board.model.MessageModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MessageModelAssembler
		extends RepresentationModelAssemblerSupport<MessageEntity, MessageModel>
		implements com.muratseyhan.message_board.service.MessageModelAssembler {
	public MessageModelAssembler() {
		super(MessageController.class, MessageModel.class);
	}

	@Override
	public MessageModel toModel(final MessageEntity messageEntity) {
		return createModelWithId(messageEntity.getId(), messageEntity)
				.add(linkTo(methodOn(MessageController.class).findAll()).withRel("messages"));
	}

	@Override
	public MessageModel instantiateModel(final MessageEntity messageEntity) {
		return new MessageModel()
				.setId(messageEntity.getId())
				.setAuthorUsername(messageEntity.getAuthor().getUsername())
				.setTitle(messageEntity.getTitle())
				.setBody(messageEntity.getBody());
	}
}
