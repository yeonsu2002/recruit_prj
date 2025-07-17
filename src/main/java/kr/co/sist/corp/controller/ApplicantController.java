package kr.co.sist.corp.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.LinkDTO;
import kr.co.sist.user.dto.ResumeResponseDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.MessageService;
import kr.co.sist.user.service.ResumeService;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApplicantController {

	private final CorpRepository corpRepos;
	private final UserRepository userRepos;

	private final ApplicantService applicantServ;
	private final ResumeService resumeServ;
	private final MessageService messageServ;

	private final CipherUtil cu;

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

	// 지원자 이력서 페이지로 넘어가기
	@GetMapping("/corp/applicant/resume/{resumeSeq}/{jobPostingSeq}")
	public String applicantResume(@PathVariable int resumeSeq, @PathVariable int jobPostingSeq, Model model,
			@AuthenticationPrincipal CustomUser corpInfo) {

		CorpEntity corpEntity = corpRepos.findById(corpInfo.getCorpNo()).orElse(null);

		// 응답용 이력서 객체
		ResumeResponseDTO resumeData = resumeServ.searchOneDetailResume(resumeSeq);

		// 사용자 가져오기
		UserEntity userEntity = userRepos.findById(resumeData.getResume().getEmail()).orElse(null);

		// 지원서 열람 처리
		int cnt = applicantServ.modifyResumeReadStatus(resumeSeq, jobPostingSeq);

		// 지원서 첫 열람시 사용자에게 메일 보내기
		if (cnt >= 0) {
			messageServ.addResumeReadNotification(userEntity, corpEntity, jobPostingSeq);
		}

		// 사용자 가공해서 바인딩
		userEntity.setPhone(cu.plainText(userEntity.getPhone()));
		userEntity.setBirth(userEntity.getBirth().substring(0, 4));
		model.addAttribute("resumeUser", userEntity);

		// 해당 지원서 스크랩 여부 가져오기
		boolean isScraped = applicantServ.isScraped((long) resumeSeq, corpEntity.getCorpNo());

		// 이력서 정보 바인딩
		model.addAttribute("resumeData", resumeData);
		model.addAttribute("resume", resumeData.getResume());
		model.addAttribute("links", resumeData.getLinks() != null ? resumeData.getLinks() : new LinkDTO());
		model.addAttribute("positions", resumeData.getPositions());
		model.addAttribute("skills", resumeData.getSkills());
		model.addAttribute("educations", resumeData.getEducations());
		model.addAttribute("careers", resumeData.getCareers());
		model.addAttribute("projects", resumeData.getProjects());
		model.addAttribute("additionals", resumeData.getAdditionals());
		model.addAttribute("introductions", resumeData.getIntroductions());
		model.addAttribute("isScraped", isScraped);
		model.addAttribute("jobPostingSeq", jobPostingSeq);

		return "/corp/applicant/applicant_resume";
	}

	// 북마크 추가
	@PostMapping("/corp/bookmark/add/{resumeSeq}")
	@ResponseBody
	public String addBookmark(@PathVariable int resumeSeq, @AuthenticationPrincipal CustomUser corpInfo) {

		CorpEntity corpEntity = corpRepos.findById(corpInfo.getCorpNo()).orElse(null);

		applicantServ.addBoomark((long) resumeSeq, corpEntity.getCorpNo());

		return "success";
	}

	// 북마크 제거
	@PostMapping("/corp/bookmark/remove/{resumeSeq}")
	@ResponseBody
	public String removeBookmark(@PathVariable int resumeSeq, @AuthenticationPrincipal CustomUser corpInfo) {

		CorpEntity corpEntity = corpRepos.findById(corpInfo.getCorpNo()).orElse(null);

		applicantServ.removeBookmark(resumeSeq, corpEntity.getCorpNo());

		return "success";
	}

	// 지원 상태 변경시 메시지 보내기
	@PostMapping("/corp/applicant/message")
	public String sendMessageToApplicant(int passStage, String messageTitle, String messageContent, int resumeSeq, String email, 
			int jobPostingSeq, @AuthenticationPrincipal CustomUser corpInfo) {

		CorpEntity corpEntity = corpRepos.findById(corpInfo.getCorpNo()).orElse(null);
		
		applicantServ.modifyPassStage(resumeSeq, jobPostingSeq, passStage); // 합격 상태 변경하기
		messageServ.addMessage(email, corpEntity.getCorpNo(), messageTitle, messageContent);

		return "redirect:/corp/applicant";
	}

}
