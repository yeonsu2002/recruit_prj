package kr.co.sist.user.service;

import org.springframework.stereotype.Service;

import kr.co.sist.login.UserRepository;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MainPageService {

    private final CipherUtil cu;
    private final UserRepository userRepository;

    public UserEntity getUserInfo(String email) {
        return userRepository.findById(email).orElse(null);
    }
}
