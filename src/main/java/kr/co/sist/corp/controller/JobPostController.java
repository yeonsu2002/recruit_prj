package kr.co.sist.corp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.UserDTO;

@Controller
public class JobPostController {
	
	private final JWTUtil jwtUtil;
	
	public JobPostController(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

  //새로우 공고등록 페이지 이동
  @GetMapping("/corp/jobPostingForm")
  public String getJobPostingForm(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
  	
  	String token = jwtUtil.resolveToken(request);
  	UserDTO userDTO = jwtUtil.validateToken(token);
  	
  	if(userDTO == null || !userDTO.getRole().equals("ROLE_CORP")) {
  		 return "redirect:/accessDenied"; //아직 안만들었어 
  	}
  	
  	model.addAttribute("user", userDTO);
  	
	 return "corp/jobPosting/jobPostingForm";
  }
  
  
  
  
  
}
