package kr.co.sist.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.ReviewDTO;
import kr.co.sist.user.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	public List<ReviewDTO> getReviews(String email){
		
		
		return reviewMapper.selectReviews(email);
	}

}



