package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostingScrapDTO;
import kr.co.sist.user.dto.MyApplicantDTO;
import kr.co.sist.user.dto.MyApplicantSearchDTO;
import kr.co.sist.user.dto.MyPostingDTO;

@Mapper
public interface MyPageMapper {

	
	public List<MyApplicantDTO> selectMyApplicant(String email);
	public List<MyApplicantDTO> selectMyAllApplicant(MyApplicantSearchDTO searchDTO);
	
	public int updateApplicationCancel(int jobApplicationSeq);
	public int deleteApplication(int jobApplicationSeq);
	
	public List<MyPostingDTO> selectMyScrapPosting(String email);
	
	public int cntMyScrapPosting(String email);
	
}
