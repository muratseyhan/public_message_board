package com.muratseyhan.message_board.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class UserInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String username;
	@NotNull
	private String password;

	public UserInfoEntity() {
	}

	public UserInfoEntity(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
