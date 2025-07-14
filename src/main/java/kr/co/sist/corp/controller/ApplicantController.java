package kr.co.sist.corp.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.corp.dto.ApplicantDTO;
import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.service.ApplicantService;
import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.CorpRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApplicantController {

	private final CorpRepository corpRepos;

	private final ApplicantService applicantServ;
	
	// 처음 지원자 관리 접근시 해당 회사의 모든 지원자 가져오기
	@GetMapping("/corp/applicant")
	public String applicant(@AuthenticationPrincipal CustomUser userInfo, Model model) {

		//기업 회원이 아니면 접근금지
		boolean hasCorpAuth = userInfo.getAuthorities().stream().anyMatch(auth -> "ROLE_CORP".equals(auth.getAuthority()));
		if (!hasCorpAuth) {
			return "redirect:/accessDenied";
		}
		
		CorpEntity corp = corpRepos.findById(userInfo.getCorpNo()).orElse(null);
		
		List<ApplicantDTO> applicants = applicantServ.searchAllApplicant(corp.getCorpNo());
		
		model.addAttribute("applicants", applicants);

		return "/corp/applicant/applicant";
	}
}
