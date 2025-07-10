package kr.co.sist.corp.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.JobPostingDTO;

@Mapper
public interface JobPostingMapper {

  //공고등록
  public int insertJobPost(JobPostingDTO jpDTO);
  
  //특정공고 가져오기
  public JobPostingDTO selectJobPost(int jobPostingSeq);
  
  //공고수정
  public int modifyJobPost(JobPostingDTO jpDTO);
  
  //공고삭제
  public int deleteJobPost(int jobPostingSeq);
  
}
