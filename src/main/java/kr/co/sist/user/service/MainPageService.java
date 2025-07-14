package kr.co.sist.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.login.UserRepository;
import kr.co.sist.user.entity.UserEntity;

@Service
public class MainPageService {

	@Autowired
	private UserRepository userRepository;
	
			  public UserEntity getUserInfo(String email) {
			    return userRepository.findById(email).orElse(null);
			}
	
}
