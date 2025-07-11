package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.user.dto.PositionCodeDTO;

@Mapper
public interface JobPostingCorpMapper {

  //공고등록
  public int insertJobPost(JobPostingDTO jpDTO);
  
  //특정공고 가져오기
  public JobPostingDTO selectJobPost(int jobPostingSeq);
  
  //공고수정
  public int modifyJobPost(JobPostingDTO jpDTO);
  
  //공고삭제
  public int deleteJobPost(int jobPostingSeq);

  //공고안에서 포지션 비동기 검색 조회 
  public List<PositionCodeDTO> selectPostionList(String keyword);

  
}
