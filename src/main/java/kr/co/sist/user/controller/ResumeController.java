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

import kr.co.sist.user.dto.ResumeDataDTO;
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

	// 이력서 폼 페이지로 이동
	@GetMapping("/user/resume/resume_form")
	public String resumePage(Model model) {

		model.addAttribute("positionList", pcs.searchAllPositionCode());

		return "/user/resume/resume_form";
	}

	// 이력서 저장하기
	@PostMapping("user/resume/resumeSubmit")
	@ResponseBody
	public Map<String, String> resumeSubmit(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestParam("resumeData") String resumeDataJson) {

		Map<String, String> result = new HashMap<>();
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			ResumeDataDTO rdd = objMapper.readValue(resumeDataJson, ResumeDataDTO.class);
			int resumeSeq = rs.addModifyResume(rdd);
			
			result.put("result", "success");
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "error");
		}
		
		return result;
	}
}
