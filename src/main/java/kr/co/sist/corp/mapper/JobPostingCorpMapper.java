package kr.co.sist.corp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.TechStackDTO;
import kr.co.sist.user.dto.UserDTO;

@Mapper
public interface JobPostingCorpMapper {

  //공고등록
  public int insertJobPost(JobPostingDTO jpDTO);
  
  //특정공고 가져오기
  public JobPostingDTO selectJobPost(int jobPostingSeq);
  
  //공고수정
  public int modifyJobPost(JobPostingDTO jpDTO);
  
  //공고삭제 (논리적삭제)
  public void finishJobPosting(int jobPostingSeq);

  //공고안에서 포지션 비동기 검색 조회 
  public List<PositionCodeDTO> selectPostionList(String keyword);
  
  //공고안에서 기술스택 비동기 검색 조회
  public List<TechStackDTO> selectTechStackList(String keyword);

  
  //나의 공고 리스트 가져오기 : 진행중, 마감, 전체 갯수 출력
  public List<Map<String, Integer>> selectMyPostingCount(Long corpNo);
  
  //나의 공고 리스트 가져오기 : 위를 제외한 전체 내용
  public List<JobPostingDTO> selectMyAllPosting(JobPostingDTO jpDTO);
  
  //이메일로 유저 정보 가져오기 
  public UserDTO selectUserInfo(String email);

}
