package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToolsController {

	@GetMapping("/user/tools/resume_coach")
	public String resumeCoach() {
		return "/user/tools/resume_coach";
	}
	
	@GetMapping("/user/tools/salary_calculator")
	public String salaryCalculator() {
		return "/user/tools/salary_calculator";
	}
	
	@GetMapping("/user/tools/unemp_calculator")
	public String severanceCalculator() {
		return "/user/tools/unemp_calculator";
	}
	
	@GetMapping("/user/tools/image_adjustment")
	public String imageAdjustment() {
		return "/user/tools/image_adjustment";
	}
}
