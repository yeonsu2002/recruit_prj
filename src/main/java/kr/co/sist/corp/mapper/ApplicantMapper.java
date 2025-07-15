package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.ApplicantDTO;
import kr.co.sist.corp.dto.ApplicantSearchDTO;
import kr.co.sist.corp.dto.JobPostingDTO;

@Mapper
public interface ApplicantMapper {

	public List<ApplicantDTO> selectAllApplicant(long corpNo);
	
	public List<ApplicantDTO> selectApplicant(ApplicantSearchDTO searchDTO);
	
	public List<JobPostingDTO> selectPostingProgress(long corpNo);
	public List<JobPostingDTO> selectPostingClosed(long corpNo);
	public List<JobPostingDTO> selectPostingAll(long corpNo);
}
