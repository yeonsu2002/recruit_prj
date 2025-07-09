package kr.co.sist.corp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobPostController {

  //새로우 공고등록 페이지 이동
  @GetMapping("/corp/jobPostingForm")
  public String getJobPostingForm() {
    return "/corp/jobPosting/jobPostingForm";
  }
  
  
  
  
  
}
