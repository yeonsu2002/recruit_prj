package kr.co.sist.admin.review;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.admin.SearchDTO;

@Controller
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
	@GetMapping("/admin/admin_review")
	public String getReviews(SearchDTO sDTO, Model model) {
	    Map<String, Object> result = adminReviewService.getReviews(sDTO);

	    if (sDTO.getPage() < 0) {
        sDTO.setPage(0);
    }
	    
	    model.addAttribute("reviewList", result.get("reviews"));
	    model.addAttribute("totalCount", result.get("total"));
	    model.addAttribute("currentPage", result.get("page"));
	    model.addAttribute("size", result.get("size"));
	    model.addAttribute("totalPages", result.get("totalPages"));

	    // 검색 조건 유지용 (필요한 경우)
	    model.addAttribute("type", sDTO.getType());
	    model.addAttribute("keyword", sDTO.getKeyword());
	    // 정렬 조건 유지용 - 이 부분 추가
	    model.addAttribute("sortField", sDTO.getSortField());
	    model.addAttribute("sortOrder", sDTO.getSortOrder());
	    
	    

	    return "admin/admin_review";
	}

}
