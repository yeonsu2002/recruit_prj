package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.admin.SearchDTO;
import kr.co.sist.user.dto.ReviewDTO;

@Mapper
public interface ReviewMapper {
	
	List<ReviewDTO> selectReviews(String email);
	
	
	/**
	 * 검색 조건, 페이징, 정렬 조건으로 조회
	 * @param dto 검색, 페이징, 정렬 
	 * @return 
	 */
	List<ReviewDTO> selectSearchReviews(SearchDTO sDTO);
	
	/**
	 * 검색 조건에 맞는 개수
	 * @param dto 검색조건
	 * @return 총 개수
	 */
	int countSearch(SearchDTO sDTO);
}
