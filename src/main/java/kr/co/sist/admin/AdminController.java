package kr.co.sist.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	
	private final AdminService as;
	
	public AdminController(AdminService as) {
		this.as = as;
	}
	@GetMapping("/admin/admin_list")
	public String loginPage(Model model) {
		List<AdminEntity> list = as.searchAllAdmin();
		model.addAttribute("adminList",list);
		return "admin/admin_list";
	}
	
	@GetMapping("admin/admin_mainpage")
	public String mainPage() {
		
		
		return "admin/admin_mainpage";
	}
}
