package kr.co.sist.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.CustomUser;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.UserDTO;

@Controller
public class MainController {
	
	private final JWTUtil jwtUtil;
	
	public MainController(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@GetMapping("/")
	public String mainPage(HttpServletRequest request) {
		
		return "redirect:/user/job_postings";
	}
	
	@GetMapping("/corp/main")
	public String corpMainPage(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {

		if(user == null) {
			System.out.println("디버깅 / 회원정보 상태 : " + "비회원, 로그인 필요 ");
		} else {
			System.out.println("디버깅 / 회원정보 상태 : " + user);
		}
		
		return "corp/main_page";
	}
}
