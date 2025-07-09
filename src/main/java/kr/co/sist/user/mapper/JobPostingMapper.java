package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostDTO;

@Mapper
public interface JobPostingMapper {

	/**
     * 모든 채용공고 조회
     */
    List<JobPostDTO> selectAllJobPostings();

	List<JobPostDTO> selectJobPostingsBySeq(Integer jobPostingSeq);
    
}
