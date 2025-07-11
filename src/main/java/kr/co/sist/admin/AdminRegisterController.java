package kr.co.sist.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminRegisterController {
	
	@GetMapping("admin_register")
	public String signUp() {
		
		return "admin/admin_register";
	}
	
	
	
}
