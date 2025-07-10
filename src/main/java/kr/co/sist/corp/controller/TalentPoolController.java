package kr.co.sist.corp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.service.TalentPoolService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TalentPoolController {
		private final TalentPoolService talentPoolService;
		
	  @GetMapping("/corp/talentPool/resume_detail")
	    public String interviewOfferPage() {
	        return "corp/talentPool/resume_detail";
	   }
	  
	  @GetMapping("/corp/talentPool/interviewOffer")
	  public String interviewOfferFragment() {
	      return "fragments/interviewOffer:: modalContent";
	  }
	  
	  @GetMapping("/corp/talentPool/talent_pool")
	    public String showTalentPool(Model model) {
	        List<TalentPoolDTO> talents = talentPoolService.getAllTalents();
	        model.addAttribute("talentPool", talents);
	        return "corp/talentPool/talent_pool"; 
	    }
}
