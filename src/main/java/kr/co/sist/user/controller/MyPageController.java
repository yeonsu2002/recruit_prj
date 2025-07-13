package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService mpService;
	
	private final JWTUtil jwtUtil;
	
	@GetMapping("/user/mypage")
	public String myPageMain(Model model) {
		
		return "/user/mypage/mypage";
				
	}
	
	@GetMapping("/user/mypage/account_resign")
	public String accountResign() {
		return "/user/mypage/account_resign";
	}
	
	@GetMapping("/user/mypage/apply_list")
	public String applyList() {
		return "/user/mypage/apply_list";
	}
	
	@GetMapping("/user/mypage/company_reviews")
	public String companyReviews() {
		return "/user/mypage/company_reviews";
	}
	
	@GetMapping("/user/mypage/favorite_companies")
	public String favoriteCompanies() {
		return "/user/mypage/favorite_companies";
	}
	
	@GetMapping("/user/mypage/mail_list")
	public String mailList() {
		return "/user/mypage/mail_list";
	}
	
	
	@GetMapping("/user/mypage/recent_posting")
	public String recentPosting() {
		return "/user/mypage/recent_posting";
	}
	
	
	@GetMapping("/user/mypage/scrap_posting")
	public String scrapPosting(HttpServletRequest request, Model model) {
		
	    
		return "/user/mypage/scrap_posting";
	}
}
