package kr.co.sist.admin.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.admin.SearchDTO;
import kr.co.sist.user.dto.ReviewDTO;
import kr.co.sist.user.mapper.ReviewMapper;

@Service
public class AdminReviewService {
	
	private final ReviewMapper reviewMapper;
	
	public AdminReviewService(ReviewMapper reviewMapper) {
		this.reviewMapper = reviewMapper;
	}
	
	public Map<String, Object> getReviews(SearchDTO sDTO){
	  // offset 계산 = page * size
    int offset = sDTO.getPage() * sDTO.getSize();

    // offset, size는 DTO에 없으므로 새 DTO 만들어 쓸 수도 있지만
    // 여기서는 Mapper XML에서 직접 계산하므로 그대로 넘김
    // 만약 별도 필드로 offset 저장한다면 dto에 추가 가능

    // 게시글 목록 조회
    List<ReviewDTO> rList = reviewMapper.selectSearchReviews(sDTO);

    // 전체 게시글 수 조회 (검색 조건만 적용)
    int total = reviewMapper.countSearch(sDTO);

    Map<String, Object> result = new HashMap<>();
    result.put("posts", rList);
    result.put("total", total);
    result.put("page", sDTO.getPage());
    result.put("size", sDTO.getSize());

    return result;
		
	}
}
