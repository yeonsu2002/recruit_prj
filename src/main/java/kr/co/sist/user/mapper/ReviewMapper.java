package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.ReviewDTO;

@Mapper
public interface ReviewMapper {
	
	List<ReviewDTO> selectReviews(String email);

}
