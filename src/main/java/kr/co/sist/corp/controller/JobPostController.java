package kr.co.sist.corp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.corp.service.JobPostingCorpService;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.UserDTO;

@Controller
public class JobPostController {
	
	private final JWTUtil jwtUtil;
	private final JobPostingCorpService jpcService;
	
	public JobPostController(JWTUtil jwtUtil, JobPostingCorpService jpcService) {
	  this.jpcService = jpcService;
		this.jwtUtil = jwtUtil;
	}

  //새로우 공고등록 페이지 이동
  @GetMapping("/corp/jobPostingForm")
  public String getJobPostingForm(Model model, HttpServletRequest request) {
  	
  	String token = jwtUtil.resolveToken(request);
  	UserDTO userDTO = jwtUtil.validateToken(token);
  	
  	if(userDTO == null || !userDTO.getRole().equals("ROLE_CORP")) {
  		 return "redirect:/accessDenied"; //아
  	}
  	
  	model.addAttribute("user", userDTO);
  	
	 return "corp/jobPosting/jobPostingForm";
  }
  
  //새로운 공고 등록
  @PostMapping("/corp/uplaodJobPosting")
  public String uploadJobPosting( JobPostingDTO jpDTO) {
  	
    jpcService.uploadJobPost(jpDTO);
    
  	return "";
  }
  //2 새로운 공고 등록
  @PostMapping("/corp/uplaodJobPosting")
  public ResponseEntity<?> registerJobPost(@RequestBody JobPostingDTO jpDTO) {
    jpcService.uploadJobPost(jpDTO);
    return ResponseEntity.ok().build();
  }
  
  //예외처리 어떻게 하지? https://chatgpt.com/s/t_686fe64224988191838976d9bfa9aea0
  
  
  
}
