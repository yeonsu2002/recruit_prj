package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobPostingController {
	
	@GetMapping("/user/job_posting")
	public String JobPostingPage() {
		
		return "user/job_posting";
	} 
	
	@GetMapping("/user/job_posting_detail")
	public String JobPostingDetailPage() {
		
		return "user/job_posting_detail";
	}
	
	@GetMapping("/user/company_info")
	public String companyInfo() {
		
		return "user/company_info";
	}
	
	@GetMapping("/user/review")
	public String review() {
		
		return "user/review";
	}


}
