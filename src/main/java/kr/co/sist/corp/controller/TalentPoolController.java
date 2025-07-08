package kr.co.sist.corp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TalentPoolController {

	  @GetMapping("/corp/talentPool/resume_detail")
	    public String interviewOfferPage() {
	        return "corp/talentPool/resume_detail";
	   }
	  
	  @GetMapping("/corp/talentPool/interviewOffer")
	  public String interviewOfferFragment() {
	      return "fragments/interviewOffer:: modalContent";
	  }

	  
}
