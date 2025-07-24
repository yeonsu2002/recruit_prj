package kr.co.sist.admin.review;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.admin.SearchDTO;

@RestController
public class AdminReviewController {
	private final AdminReviewService adminReviewService;
	
	public AdminReviewController(AdminReviewService adminReviewService) {
		this.adminReviewService = adminReviewService;
	}
	
	
	/**
   * 게시글 검색, 페이징, 정렬 API
   * @param dto 쿼리 파라미터가 DTO 필드로 자동 바인딩 됨
   * @return Map 형태로 게시글 리스트 + 페이징 정보 리턴
   */
	@GetMapping("/select/reviews")
	public Map<String, Object> getReviews(SearchDTO sDTO){
		return adminReviewService.getReviews(sDTO);
	}
}


