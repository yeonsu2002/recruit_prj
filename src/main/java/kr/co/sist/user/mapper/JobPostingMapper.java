package kr.co.sist.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.user.dto.JobPostDTO;

@Mapper
public interface JobPostingMapper {

  // 전체 공고 조회
  List<JobPostDTO> selectAllJobPostings();
  
  // 특정 공고 조회
  List<JobPostDTO> selectJobPostingsBySeq(Integer jobPostingSeq);
  
  // 특정 공고의 기술 스택 조회
  List<String> selectTechStacksByJobPostingSeq(Integer jobPostingSeq);
  
  // 조회수 업데이트
  void updateJobPostingViewCount(@Param("jobPostingSeq") Integer jobPostingSeq, @Param("viewCount") Integer viewCount);
  
  // 인기순 공고 가져오기 (조회수 기준)
  List<JobPostDTO> getPopularJobPostings();
  
  //특정 기업의 채용공고 목록 조회
  List<JobPostDTO> selectJobPostingsByCorpNo(long corpNo);
  
  //특정 기업의 활성화된 채용공고 조회 (현재 공고 제외)
  List<JobPostDTO> selectCompanyActiveJobs(Map<String, Object> params);

}