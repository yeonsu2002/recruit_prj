package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostingScrapDTO;

@Mapper
public interface MyPageMapper {

	public List<JobPostingScrapDTO> selectScrapPosting(String email);
	
}
