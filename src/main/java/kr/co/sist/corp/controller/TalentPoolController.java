package kr.co.sist.corp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TalentPoolController {

	  @GetMapping("/corp/all_candidate")
	    public String allCandidate(Model model) {

	        return "corp/all_candidate";
	  }
	
}
