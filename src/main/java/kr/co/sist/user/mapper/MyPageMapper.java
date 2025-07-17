package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostingScrapDTO;
import kr.co.sist.user.dto.MyApplicantDTO;

@Mapper
public interface MyPageMapper {

	public List<JobPostingScrapDTO> selectScrapPosting(String email);
	
	public List<MyApplicantDTO> selectMyApplicant(String email);
	
	public int updateApplicationCancel(int jobApplicationSeq);
	
}
