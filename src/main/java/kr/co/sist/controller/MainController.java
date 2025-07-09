package kr.co.sist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.JWTUtil;

@Controller
public class MainController {
	
	private final JWTUtil jwtUtil;
	
	public MainController(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@GetMapping("/")
	public String mainPage(HttpServletRequest request) {
		
		String token = jwtUtil.resolveToken(request);
		
		System.out.print("토큰이오!!! = ");
		System.out.println(token);
		
		//if(token != null && )
		
		return "user/main_page";
	}
	
	@GetMapping("/corp/main")
	public String corpMainPage() {
		return "corp/main_page";
	}
}
