package com.muratseyhan.message_board.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class MessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Embedded
	@ManyToOne
	private UserInfoEntity author;

	private String title;
	private String body;

	public MessageEntity() {
	}
}
