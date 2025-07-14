package kr.co.sist.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.AttachmentDTO;
import kr.co.sist.user.dto.LinkDTO;
import kr.co.sist.user.dto.ResumeDTO;
import kr.co.sist.user.dto.ResumeRequestDTO;
import kr.co.sist.user.dto.ResumeResponseDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.service.PositionCodeService;
import kr.co.sist.user.service.ResumeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ResumeController {

	private final ResumeService rServ;
	private final PositionCodeService pcs;
	private final ObjectMapper objMapper;
	
	private final JWTUtil jwtUtil;


	// 이력서 관리 페이지로 이동
	@GetMapping("/user/resume/resume_management")
	public String resumeManagementPage(HttpServletRequest request, Model model) {

		String token = jwtUtil.resolveToken(request);
		UserDTO user = jwtUtil.validateToken(token);

		// 임시로 유저 등록
//		UserEntity user = uRepos.findById("juhyunsuk@naver.com").orElse(null);
		
	    List<ResumeDTO> resumes = rServ.searchAllResumeByUser(user.getEmail());

		List<AttachmentDTO> files = rServ.searchAllAttachment(user.getEmail());

		model.addAttribute("user", user);
		model.addAttribute("resumes", resumes);
		model.addAttribute("files", files);

		return "/user/resume/resume_management";
	}

	// 이력서 신규 작성
	@GetMapping("/user/resume/resume_create")
	public String resumeCreate(Model model, HttpServletRequest request) {

		String token = jwtUtil.resolveToken(request);
		UserDTO user = jwtUtil.validateToken(token);

		int resumeSeq = rServ.addResume(user);
		
		return "redirect:/user/resume/resume_form/" + resumeSeq;
	}

	// 이력서 폼으로 넘어가기(신규 작성 후 or 기존 이력서 클릭)
	@GetMapping("/user/resume/resume_form/{resumeSeq}")
	public String resumeForm(@PathVariable int resumeSeq, Model model, HttpServletRequest request) {

		// 임시로 유저 등록
//		UserEntity user = uRepos.findById("juhyunsuk@naver.com").orElse(null);
//		user.setName(cu.plainText(user.getName()));
//		user.setPhone(cu.plainText(user.getPhone()));
//		user.setBirth(user.getBirth().substring(0, 4));

		String token = jwtUtil.resolveToken(request);
		UserDTO user = jwtUtil.validateToken(token);
		model.addAttribute("user", user);

		model.addAttribute("positionList", pcs.searchAllPositionCode());
		ResumeResponseDTO resumeData = rServ.searchOneDetailResume(resumeSeq);
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
	public Map<String, String> resumeSubmit(@RequestParam(required = false) MultipartFile profileImage,
			@RequestParam("resumeData") String resumeDataJson) {

		Map<String, String> result = new HashMap<>();

		try {
			ResumeRequestDTO rdd = objMapper.readValue(resumeDataJson, ResumeRequestDTO.class);
			rServ.modifyResume(rdd, profileImage, rdd.getBasicInfo().getResumeSeq());

			result.put("result", "success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "error");
		}

		return result;
	}// resumeSubmit

	// 이력서 미리보기
	@PostMapping("/user/resume/resumePreview")
	@ResponseBody
	public Map<String, Object> resumePreview(@RequestParam(required = false) MultipartFile profileImg,
			@RequestParam("resumeData") String resumeDataJson) {
		Map<String, Object> result = new HashMap<>();
		try {
			ResumeRequestDTO rdd = objMapper.readValue(resumeDataJson, ResumeRequestDTO.class);
			rServ.modifyResume(rdd, profileImg, rdd.getBasicInfo().getResumeSeq());

			String previewUrl = "/user/resume/preview/" + rdd.getBasicInfo().getResumeSeq();

			result.put("result", "success");
			result.put("previewUrl", previewUrl);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "error");
		}
		return result;
	}

	// 이력서 미리보기 창 띄우기
	@GetMapping("/user/resume/preview/{resumeSeq}")
	public String resumePreviewPage(@PathVariable int resumeSeq, Model model, HttpServletRequest request) {

		// 임시로 유저 등록
		String token = jwtUtil.resolveToken(request);
		UserDTO user = jwtUtil.validateToken(token);
		model.addAttribute("user", user);

		ResumeResponseDTO resumeData = rServ.searchOneDetailResume(resumeSeq);

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

		return "/user/resume/resume_preview"; // 실제 보여줄 미리보기 페이지 뷰 이름
	}

	// 이력서 삭제하기
	@PostMapping("/user/resume/resumeRemove/{resumeSeq}")
	@ResponseBody
	public Map<String, Object> resumeRemove(@PathVariable int resumeSeq) {

		Map<String, Object> result = new HashMap<>();

		rServ.removeResume(resumeSeq);
		
		result.put("result", "success");

		return result;
	}

}// class
