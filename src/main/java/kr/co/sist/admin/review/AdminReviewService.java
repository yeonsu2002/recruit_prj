package kr.co.sist.admin.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import kr.co.sist.admin.SearchDTO;
import kr.co.sist.user.mapper.ReviewMapper;

@Service
public class AdminReviewService {
	
	private final ReviewMapper reviewMapper;
	
	public AdminReviewService(ReviewMapper reviewMapper) {
		this.reviewMapper = reviewMapper;
	}
	
	public Map<String, Object> getReviews(SearchDTO sDTO){
		// offset 계산: page * size (page가 0부터 시작하므로)
		int offset = sDTO.getPage() * sDTO.getSize();
		
		// SearchDTO에 offset 설정
		sDTO.setOffset(offset);
		
		// 리뷰 목록 조회
		List<AdminReviewDTO> rList = reviewMapper.selectSearchReviews(sDTO);
		
		// 전체 리뷰 수 조회 (검색 조건만 적용, 페이징 제외)
		int total = reviewMapper.countSearch(sDTO);
		
		// 결과 맵 구성
		Map<String, Object> result = new HashMap<>();
		result.put("reviews", rList);  
		result.put("total", total);
		result.put("page", sDTO.getPage());
		result.put("size", sDTO.getSize());
		result.put("totalPages", (int) Math.ceil((double) total / sDTO.getSize())); // 총 페이지 수 추가
		
		return result;
	}
}