package kr.co.sist.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.service.JobPostingService;

@Controller
public class JobPostingController {
    
 
	private final JobPostingService jps;
	
	
	@Autowired
	public JobPostingController(JobPostingService jps) {
		this.jps=jps;
	}
	
	@GetMapping("/user/job_posting/job_posting")
	public String getJobPostings(@RequestParam(required = false) Integer jobPostingSeq, Model model) {
	    List<JobPostDTO> jobPostingList = jps.getJobPostings(jobPostingSeq);  // jobPostingSeq에 맞는 공고를 조회
	    model.addAttribute("jobPostingList", jobPostingList);  // 모델에 추가
		
		return "user/job_posting/job_posting";
	}
	
		
	
	//공고 상세보기
	@GetMapping("/user/job_posting/job_posting_detail")
	public String JobPostingDetailPage(@RequestParam(required = false) Integer jobPostingSeq, Model model) {
		
		
		
		JobPostDTO jDto=jps.findById(jobPostingSeq);
		model.addAttribute("jDto", jDto);
		 System.out.println("Job Posting Detail: " + jDto.getTechStacks()); 
		return "user/job_posting/job_posting_detail";
	}
	
	
	
	@GetMapping("/user/job_posting/company_info")
	public String companyInfo() {
		
		return "user/job_posting/company_info";
	}
	
	@GetMapping("/user/job_posting/review")
	public String review() {
		
		return "user/job_posting/review";
	}


}
