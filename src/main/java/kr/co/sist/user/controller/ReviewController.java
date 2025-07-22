package kr.co.sist.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.jwt.CustomUser;
import kr.co.sist.user.dto.ReviewDTO;
import kr.co.sist.user.service.ReviewService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService rs;
	
	 //기업리뷰
  @GetMapping("/user/job_posting/review")
  public String review(Model model, @AuthenticationPrincipal CustomUser userInfo) {
  	
  	 String email = userInfo.getEmail();
  	 List<ReviewDTO> reviewList=rs.getReviews(email);
  	
  	 model.addAttribute("reviewList", reviewList);
  	
  	
      return "user/job_posting/review";
  }
  
  //기업리뷰
  @GetMapping("/user/job_posting/review_write")
  public String reviewWrite() {
  	return "user/job_posting/review_write";
  }
	
}
