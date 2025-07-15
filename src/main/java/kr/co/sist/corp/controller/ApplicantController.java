package kr.co.sist.corp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.corp.dto.ApplicantDTO;
import kr.co.sist.corp.dto.ApplicantSearchDTO;
import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.dto.JobPostingDTO;
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

		// 기업 회원이 아니면 접근금지
		boolean hasCorpAuth = userInfo.getAuthorities().stream().anyMatch(auth -> "ROLE_CORP".equals(auth.getAuthority()));
		if (!hasCorpAuth) {
			return "redirect:/accessDenied";
		}

		CorpEntity corp = corpRepos.findById(userInfo.getCorpNo()).orElse(null);

		List<ApplicantDTO> applicants = applicantServ.searchAllApplicant(corp.getCorpNo());

		model.addAttribute("applicants", applicants);

		return "/corp/applicant/applicant";
	}// applicant

	// 공고 유형 선택에 따른 결과값 보내기(진행중, 마감된 공고)
	@PostMapping("/corp/applicant")
	@ResponseBody
	public Map<String, Object> getPostingTitle(@RequestParam(required = false, defaultValue = "") String status,
			@AuthenticationPrincipal CustomUser userInfo) {

		CorpEntity corp = corpRepos.findById(userInfo.getCorpNo()).orElse(null);

		Map<String, Object> result = new HashMap<String, Object>();
		List<JobPostingDTO> postings = new ArrayList<>();
		String defaultOption = "전체 공고";

		switch (status) {
		case "":
			postings = applicantServ.searchPostingAll(corp.getCorpNo());
			defaultOption = "전체 공고";
			break;
		case "progress":
			postings = applicantServ.searchPostingProgress(corp.getCorpNo());
			defaultOption = "전체 진행중 공고";
			break;
		case "closed":
			postings = applicantServ.searchPostingClosed(corp.getCorpNo());
			defaultOption = "전체 마감된 공고";
			break;
		}

		result.put("defaultOption", defaultOption);
		result.put("postings", postings);

		return result;
	}// getPostingTitle

	// 지원자 조건 검색
	@PostMapping("/corp/applicant/search")
	@ResponseBody
	public Map<String, Object> applicantSearch(@RequestBody ApplicantSearchDTO searchDTO,
			@AuthenticationPrincipal CustomUser userInfo) {
		Map<String, Object> result = new HashMap<String, Object>();

		CorpEntity corp = corpRepos.findById(userInfo.getCorpNo()).orElse(null);
		searchDTO.setCorpNo(corp.getCorpNo());

		List<ApplicantDTO> applicants = applicantServ.searchApplicant(searchDTO);

		result.put("applicants", applicants);

		return result;
	}// applicantSearch
	
	//지원자 이력서 페이지로 넘어가기
	@GetMapping("/corp/applicant/resume/{resumeSeq}")
	public String applicantResume(@PathVariable int resumeSeq) {
		
		return "";
	}
}
