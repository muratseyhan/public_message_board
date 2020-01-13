package com.muratseyhan.message_board.service;

import com.muratseyhan.message_board.data.entity.UserInfoEntity;
import com.muratseyhan.message_board.data.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfoEntity user = userInfoRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("User '%s' could not be found.", username));
		}

		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
}
