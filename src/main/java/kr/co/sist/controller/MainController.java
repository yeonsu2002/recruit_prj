package kr.co.sist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String mainPage() {
		return "user/main_page";
	}
	
	@GetMapping("/corp/main")
	public String corpMainPage() {
		return "corp/main_page";
	}
}
