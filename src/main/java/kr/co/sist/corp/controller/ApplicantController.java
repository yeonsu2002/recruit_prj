package kr.co.sist.corp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicantController {

	@GetMapping("/corp/applicant")
	public String applicant() {
		
		return "/corp/applicant/applicant";
	}
}
