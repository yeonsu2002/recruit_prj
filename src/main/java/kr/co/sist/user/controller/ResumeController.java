package kr.co.sist.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.sist.user.dto.LinkDTO;
import kr.co.sist.user.dto.ResumeRequestDTO;
import kr.co.sist.user.dto.ResumeResponseDTO;
import kr.co.sist.user.service.PositionCodeService;
import kr.co.sist.user.service.ResumeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ResumeController {

	private final ResumeService rs;
	private final PositionCodeService pcs;

	// 이력서 관리 페이지로 이동
	@GetMapping("/user/resume/resume_management")
	public String resumeManagementPage() {

		return "/user/resume/resume_management";
	}

	// 이력서 신규 작성
	@GetMapping("/user/resume/resume_create")
	public String resumeCreate(Model model) {

		int resumeSeq = rs.addResume();

		return "redirect:/user/resume/resume_form?seq=" + resumeSeq;
	}

	// 이력서 폼으로 넘어가기(신규 작성 후 or 기존 이력서 클릭)
	@GetMapping("/user/resume/resume_form")
	public String resumeForm(@RequestParam int seq, Model model) {

		model.addAttribute("positionList", pcs.searchAllPositionCode());
		ResumeResponseDTO resumeData = rs.searchOneDetailResume(seq);
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

		return "/user/resume/resume_form";
	}

	// 이력서 저장(수정)하기
	@PostMapping("/user/resume/resumeSubmit")
	@ResponseBody
	public Map<String, String> resumeSubmit(
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestParam("resumeData") String resumeDataJson, int resumeSeq) {

		Map<String, String> result = new HashMap<>();
		ObjectMapper objMapper = new ObjectMapper();

		try {
			ResumeRequestDTO rdd = objMapper.readValue(resumeDataJson, ResumeRequestDTO.class);
			rs.modifyResume(rdd, profileImage, resumeSeq);

			result.put("result", "success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "error");
		}

		return result;
	}// resumeSubmit

}
