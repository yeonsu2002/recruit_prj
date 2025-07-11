package kr.co.sist.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.sist.login.UserRepository;
import kr.co.sist.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	private final UserRepository uRepos;
	
	@GetMapping("/user/mypage")
	public String myPageMain(Model model) {
		
		UserEntity user = uRepos.findById("juhyunsuk@naver.com").orElse(null);
		
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
	@GetMapping("/user/mypage/notifications")
	public String notifications() {
		return "/user/mypage/notifications";
	}
	@GetMapping("/user/mypage/recent_posting")
	public String recentPosting() {
		return "/user/mypage/recent_posting";
	}
	@GetMapping("/user/mypage/suggested_positions")
	public String suggestedPositions() {
		return "/user/mypage/suggested_positions";
	}
	@GetMapping("/user/mypage/favorite_posting")
	public String favoritePosting() {
		return "/user/mypage/favorite_posting";
	}
}
