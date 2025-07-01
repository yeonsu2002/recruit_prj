package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResumeController {

	@GetMapping("/user/resume/resume_management")
	public String resumeManagementPage() {
		
		return "/user/resume/resume_management";
	}
	
	@GetMapping("/user/resume/resume_form")
	public String resumePage() {
		
		return "/user/resume/resume_form";
	}
}
