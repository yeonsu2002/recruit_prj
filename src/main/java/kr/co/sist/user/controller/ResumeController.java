package kr.co.sist.user.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.service.ResumeService;

@Controller
public class ResumeController {
	
	private final ResumeService rs;
	
	public ResumeController(ResumeService rs) {
		this.rs = rs;
	}
	
	//이력서 관리 페이지로 이동
	@GetMapping("/user/resume/resume_management")
	public String resumeManagementPage() {
		
		return "/user/resume/resume_management";
	}
	
	//이력서 폼 페이지로 이동
	@GetMapping("/user/resume/resume_form")
	public String resumePage(Model model) {
		
		List<PositionCodeDTO> list = rs.searchAllPositionCode();
	    System.out.println("searchAllPositionCode 호출 결과: " + list);
	    System.out.println("안녕하세요@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		model.addAttribute("pcList", rs.searchAllPositionCode());
		
		return "/user/resume/resume_form";
	}
}
