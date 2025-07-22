package kr.co.sist.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final AdminService as;
	
	public AdminController(AdminService as) {
		this.as = as;
	}
	
	@GetMapping("/admin_list")
	public String adminList(
			@RequestParam(defaultValue = "전체") String searchType,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "전체") String dept,
			@RequestParam(defaultValue = "전체") String job,
			@RequestParam(defaultValue = "전체") String stat,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			Model model) {
		
		Page<AdminEntity> adminPage = as.searchAdminsWithFilters(
			searchType, keyword, dept, job, stat, page, size);
		
		model.addAttribute("adminList", adminPage.getContent());
		model.addAttribute("totalPages", adminPage.getTotalPages());
		model.addAttribute("currentPage", adminPage.getNumber());
		model.addAttribute("totalCount", adminPage.getTotalElements());
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("dept", dept);
		model.addAttribute("job", job);
		model.addAttribute("stat", stat);

		return "admin/admin_list";
	}
	
	@GetMapping("/admin_mainpage")
	public String mainPage() {
		return "admin/admin_mainpage";
	}

	// 상태 변경 처리 메서드 (POST)
	@PostMapping("/updateStatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateStatus(@RequestBody Map<String, Object> payload) {
	    List<String> adminIds = (List<String>) payload.get("adminIds");
	    String status = (String) payload.get("status");

	    Map<String, Object> result = new HashMap<>();

	    try {
	        as.updateAdminStatus(adminIds, status);
	        result.put("success", true);
	        result.put("message", "상태가 성공적으로 변경되었습니다.");
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("success", false);
	        result.put("message", "상태 변경 중 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	    }
	}
}
