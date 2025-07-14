package kr.co.sist.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.JobPostingScrapDTO;
import kr.co.sist.user.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final MyPageMapper mpMapper;
	
	public List<JobPostingScrapDTO> searchScrapPosting(String email){
		return mpMapper.selectScrapPosting(email);
	}
	
}
