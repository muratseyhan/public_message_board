package com.muratseyhan.message_board.data.repository;

import com.muratseyhan.message_board.data.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
	Optional<MessageEntity> findByIdAndAuthorUsername(Long id, String authorUsername);
}
