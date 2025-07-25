package kr.co.sist.admin.review;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminReviewRestController {
	
	private final AdminReviewService adminReviewService;
	public AdminReviewRestController(AdminReviewService adminReviewService) {
		
		this.adminReviewService = adminReviewService;
	}
	
	@DeleteMapping("/admin/deleteReview")
	public String deleteReview(@RequestBody Map<String, Object> map) {
    
		
		 System.out.println("전체 map: " + map);
     System.out.println("reviewSeq 존재 여부: " + map.containsKey("reviewSeq"));
     System.out.println("reviewSeq 값: " + map.get("reviewSeq"));
     System.out.println("reviewSeq 타입: " + (map.get("reviewSeq") != null ? map.get("reviewSeq").getClass() : "null"));
    boolean deleted = adminReviewService.deleteReview(map);
    
    if (deleted) {
        return "삭제 성공";
    } else {
        return "삭제 실패: 리뷰 없음";
    }
}
}


