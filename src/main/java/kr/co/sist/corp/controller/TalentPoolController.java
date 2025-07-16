package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
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
	  public String showTalentPool(@RequestParam(defaultValue = "1") int page,
	                               @RequestParam(defaultValue = "5") int size,
	                               Model model,
	                               HttpSession session) {

	      Long corpNo = (Long) session.getAttribute("corpNo");
	      if (corpNo == null) {
	          corpNo = 103L;
	      }

	      int offset = (page - 1) * size;
	      List<TalentPoolDTO> talents = talentPoolService.getPaginatedTalents(offset, size, corpNo);
	      int totalCount = talentPoolService.selectTalentTotalCount();
	      int totalPages = (int) Math.ceil((double) totalCount / size);

	      model.addAttribute("talentPool", talents);
	      model.addAttribute("totalCount", totalCount);
	      model.addAttribute("page", page);
	      model.addAttribute("pageSize", size);
	      model.addAttribute("totalPages", totalPages);

	      return "corp/talentPool/talent_pool";
	  }

	  @GetMapping("/corp/talentPool/scrap")
	  public String showScrappedTalents(Model model) {
		  Long corpNo = 103L;
		  
		  List<TalentPoolDTO> sTalents = talentPoolService.getScrappedTalents(corpNo);
		  model.addAttribute("talentPool", sTalents);
		  model.addAttribute("totalCount", sTalents.size());
		  
		  
		  return "corp/talentPool/scrap";
	  }

	  @PostMapping("/corp/talentPool/scrap")
	  @ResponseBody
	  public ResponseEntity<Map<String, String>> scrapResume(@RequestBody Map<String, Long> payload) {
	      Long resumeSeq = payload.get("resumeSeq");
	      Long corpNo = 103L;
	      String status = talentPoolService.scrapResume(resumeSeq, corpNo);

	      Map<String, String> response = new HashMap<>();
	      response.put("status", status);
	      return ResponseEntity.ok(response);
	  }


	  
	  
	  @GetMapping("/corp/talentPool/offer")
	  public String showOfferTalents(Model model) {
		  return "corp/talentPool/offer";
	  }
	  @GetMapping("/corp/talentPool/recently_viewed")
	  public String showRecentlyViewedTalents(Model model) {
		  return "corp/talentPool/recently_viewed";
	  }
	  
}
