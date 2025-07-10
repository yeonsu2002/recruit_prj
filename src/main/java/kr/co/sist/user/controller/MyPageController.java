package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.login.UserRepository;
import kr.co.sist.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	private final UserRepository uRepos;
	
	@GetMapping("/user/mypage")
	public String myPagePage(Model model) {
		
		UserEntity user = uRepos.findById("juhyunsuk@naver.com").orElse(null);
		
		return "/user/mypage/mypage";
				
	}
}
