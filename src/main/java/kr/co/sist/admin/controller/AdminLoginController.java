package kr.co.sist.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {
	
	@GetMapping("/admin_login")
	public String loginPage() {
		
		return "admin/admin_login";
	}
	

}
