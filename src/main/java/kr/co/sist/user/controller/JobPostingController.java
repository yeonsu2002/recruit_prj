package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobPostingController {
	
	@GetMapping("/user/job_posting")
	public String JobPostingPage() {
		
		return "/user/job_posting";
	}

}
