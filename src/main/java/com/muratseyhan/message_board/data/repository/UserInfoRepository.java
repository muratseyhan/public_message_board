package com.muratseyhan.message_board.data.repository;

import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
	Boolean existsByUsername(String username);

	UserInfoEntity findByUsername(String username);
}
