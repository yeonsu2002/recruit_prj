package kr.co.sist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.UserDTO;

@Controller
public class MainController {
	
	private final JWTUtil jwtUtil;
	
	public MainController(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@GetMapping("/")
	public String mainPage(HttpServletRequest request, Model model) {
		
		String token = jwtUtil.resolveToken(request);
		UserDTO uDTO = jwtUtil.validateToken(token);
		
		model.addAttribute("user", uDTO);
		
		return "user/main_page";
	}
	
	@GetMapping("/corp/main")
	public String corpMainPage(HttpServletRequest request, Model model) {
	  
	  String token = jwtUtil.resolveToken(request);
    UserDTO uDTO = jwtUtil.validateToken(token);
    
    model.addAttribute("user", uDTO);
    
		return "corp/main_page";
	}
}
