package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.user.dto.JobPostDTO;

@Mapper
public interface JobPostingMapper {

  // 전체 공고 조회
  List<JobPostDTO> selectAllJobPostings();
  
  // 특정 공고 조회
  JobPostDTO selectJobPostingsBySeq(Integer jobPostingSeq);
  
  // 특정 공고의 기술 스택 조회
  List<String> selectTechStacksByJobPostingSeq(Integer jobPostingSeq);
  
  // 조회수 업데이트
  void updateJobPostingViewCount(@Param("jobPostingSeq") Integer jobPostingSeq, @Param("viewCount") Integer viewCount);
  
  // 인기순 공고 가져오기 (조회수 기준)
  List<JobPostDTO> getPopularJobPostings();

}