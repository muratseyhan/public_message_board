package com.muratseyhan.message_board.controller;

import com.muratseyhan.message_board.error.ErrorResponseEntityFactory;
import com.muratseyhan.message_board.model.MessageRequest;
import com.muratseyhan.message_board.model.MessageModel;
import com.muratseyhan.message_board.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("messages")
public class MessageController {
	@Autowired
	private MessageService messageService;

	@Autowired
	private ErrorResponseEntityFactory errorResponseEntityFactory;

	@GetMapping
	public HttpEntity<CollectionModel<MessageModel>> findAll() {
		return ResponseEntity.ok(messageService.findAllMessages());
	}

	@PostMapping
	public HttpEntity<MessageModel> create(@Valid @RequestBody MessageRequest messageRequest,
										   Authentication authentication) {
		final MessageModel messageModel = messageService.createMessage(messageRequest, authentication.getName());

		return ResponseEntity.created(messageModel.getRequiredLink("self").toUri())
				.build();
	}

	@GetMapping("/{id}")
	public HttpEntity<?> findOne(@PathVariable Long id) {
		final MessageModel messageModel = messageService.findMessage(id);

		if (messageModel != null) {
			return ResponseEntity.ok(messageModel);
		}

		return errorResponseEntityFactory.createMessageNotFoundResponseEntity(id);
	}

	@PutMapping("/{id}")
	public HttpEntity<?> update(@PathVariable Long id, @Valid @RequestBody MessageRequest messageRequest,
								Authentication authentication) {
		if (messageService.updateMessage(id, messageRequest, authentication.getName())) {
			return ResponseEntity.noContent().build();
		}

		return errorResponseEntityFactory.createUnauthorizedResponseEntity();
	}

	@DeleteMapping("/{id}")
	public HttpEntity<?> delete(@PathVariable Long id,
								Authentication authentication) {
		if (messageService.deleteMessage(id, authentication.getName())) {
			return ResponseEntity.noContent().build();
		}

		return errorResponseEntityFactory.createUnauthorizedResponseEntity();
	}
}