package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostDTO;

@Mapper
public interface JobPostingMapper {


    List<JobPostDTO> selectAllJobPostings();
    
    List<JobPostDTO> selectJobPostingsBySeq(Integer jobPostingSeq);
    
    // 수정: List<JobPostDTO> 반환으로 변경
    JobPostDTO selectJobPostingById(Integer jobPostingSeq); // 반환 타입 변경
    
    // 특정 공고에 대한 기술 스택 조회
    List<String> selectTechStacksByJobPostingSeq(Integer jobPostingSeq);
}
