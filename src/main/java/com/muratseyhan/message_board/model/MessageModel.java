package com.muratseyhan.message_board.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class MessageModel extends RepresentationModel<MessageModel> {
	private Long id;
	private String authorUsername;
	private String title;
	private String body;
}