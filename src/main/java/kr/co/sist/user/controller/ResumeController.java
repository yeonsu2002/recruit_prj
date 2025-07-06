package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.user.service.PositionCodeService;
import kr.co.sist.user.service.ResumeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ResumeController {
	
	private final ResumeService rs;
	private final PositionCodeService pcs;
	
	
	//이력서 관리 페이지로 이동
	@GetMapping("/user/resume/resume_management")
	public String resumeManagementPage() {
		
		return "/user/resume/resume_management";
	}
	
	//이력서 폼 페이지로 이동
	@GetMapping("/user/resume/resume_form")
	public String resumePage(Model model) {
		
		model.addAttribute("positionList", pcs.searchAllPositionCode());
		
		return "/user/resume/resume_form";
	}
}
