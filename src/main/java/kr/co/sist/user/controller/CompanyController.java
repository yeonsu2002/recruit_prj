package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.user.dto.CompanyDTO;
import kr.co.sist.user.service.CompanyService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyController {

		private final CompanyService cms;
	
	  @GetMapping("/user/job_posting/company_info")
		public String getCompanyInfo(@RequestParam(required = true) long corpNo, Model model) {
			
	  	CompanyDTO companyDTO = cms.getCompanyInfoByCorpNo(corpNo);
	  	
	  	model.addAttribute("companyDTO", companyDTO);
	  	
	  	
	  	return "user/job_posting/company_info";
		}
	
}
