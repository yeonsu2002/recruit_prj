package kr.co.sist.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.MessageDTO;
import kr.co.sist.user.dto.MyApplicantDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.MessageService;
import kr.co.sist.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageServ;
	private final MessageService messageServ;
	
	private final UserRepository userRepos;
	
	@GetMapping("/user/mypage")
	public String myPageMain(Model model) {
		
		return "/user/mypage/mypage";
				
	}
	
	@GetMapping("/user/mypage/account_resign")
	public String accountResign() {
		return "/user/mypage/account_resign";
	}
	
	//지원 목록으로 이동
	@GetMapping("/user/mypage/apply_list")
	public String applyList(@AuthenticationPrincipal CustomUser userInfo, Model model) {
		
		
		UserEntity userEntity = userRepos.findById(userInfo.getEmail()).orElse(null);
		
		//전체 지원목록 가져오기
		List<MyApplicantDTO> applicantDTO = myPageServ.searchMyApplicant(userEntity.getEmail());
		
		// 통계용 변수
    int completed = 0;   // 지원완료 (passStage == 0)
    int passed = 0;      // 최종합격 (passStage == 2)
    int failed = 0;      // 불합격 (passStage == 3)
    int docPassed = 0;   // 서류 통과 (passStage == 1)
    
    for (MyApplicantDTO dto : applicantDTO) {
        if (dto.getApplicationStatus() == 2) continue; // 지원취소는 제외

        switch (dto.getPassStage()) {
            case 0: completed++; break;
            case 1: docPassed++; break;
            case 2: passed++; break;
            case 3: failed++; break;
        }
    }

    model.addAttribute("applicants", applicantDTO);
    model.addAttribute("completed", completed);
    model.addAttribute("docPassed", docPassed);
    model.addAttribute("passed", passed);
    model.addAttribute("failed", failed);
		model.addAttribute("applicants", applicantDTO);
		
		return "/user/mypage/apply_list";
	}
	
	//메일 목록으로 이동
	@GetMapping("/user/mypage/mail_list")
	public String mailList(@AuthenticationPrincipal CustomUser userInfo, Model model) {
		
		UserEntity userEntity = userRepos.findById(userInfo.getEmail()).orElse(null);
		
		//모든 메일목록 가져오기
		List<MessageDTO> messages = messageServ.searchMyMessage(userEntity.getEmail());
		
		model.addAttribute("messages", messages);
		
		return "/user/mypage/mail_list";
	}
	
	@GetMapping("/user/mypage/company_reviews")
	public String companyReviews() {
		return "/user/mypage/company_reviews";
	}
	
	@GetMapping("/user/mypage/favorite_companies")
	public String favoriteCompanies() {
		return "/user/mypage/favorite_companies";
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