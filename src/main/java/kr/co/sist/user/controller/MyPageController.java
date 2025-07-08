package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

	@GetMapping("/user/mypage")
	public String myPagePage() {
		
		return "/user/mypage/mypage";
				
	}
}
