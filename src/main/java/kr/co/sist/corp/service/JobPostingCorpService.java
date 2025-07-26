package kr.co.sist.corp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;

import kr.co.sist.corp.dto.CorpDTO;
import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.dto.JobPostingApplicantStatsDTO;
import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.corp.dto.JobPostingEntity;
import kr.co.sist.corp.mapper.JobPostingCorpMapper;
import kr.co.sist.corp.mapper.JobPostingTechStackMapper;
import kr.co.sist.corp.repository.JobPostingRepository;
import kr.co.sist.globalController.Exceptions.LoginException;
import kr.co.sist.globalController.Exceptions.NotFoundException;
import kr.co.sist.login.CorpRepository;
import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.TechStackDTO;
import kr.co.sist.user.entity.UserEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //final 생성자주입 자동
public class JobPostingCorpService {

  private final JobPostingCorpMapper jpm;
  private final JobPostingTechStackMapper jptm;
  private final CorpRepository cRepository;
  private final JobPostingRepository jRepository;
  
  /**
   * 공고 새로 등록하기 
   * 연속적인 insert는 항상 서비스단에서 묶어서 처리하고, @Transactional을 걸어야 안전빵
   * @param jpDTO
   * @return
   */
  @Transactional 
  public void uploadJobPost(JobPostingDTO jpDTO) {
  	
  	try {
  		//1. 공고등록
      jpm.insertJobPost(jpDTO);
      
      /**
       * jpDTO.getJobPostingSeq() 왜 돼?
       * jpDTO는 참조변수
       * mapper내부에서 setter(.setJobPostingSeq(123)) 이런 setter를 호출했꼬, 
       *  (keyProperty="jobPostingSeq" => DTO의 해당 필드에 자동 주입)
       * 나는 그 참조변수를다시 가져와서 보는 것이니 매개변수때와 그 값이 다르다. 
       * 따라서 selectKey를 안쓰면 못가져와 
       */
      int jobPostingSeq = jpDTO.getJobPostingSeq();  
  		
      //2. 기술 스택등록(중간테이블)
      for(Integer techStackSeq : jpDTO.getTechStackSeqList()) {
      	jptm.insertjobPostingTechStack(jobPostingSeq, techStackSeq);
      }
      
		} catch (Exception e) {
			//로그출력 
			e.printStackTrace();
			//트랜잭션 롤백 유도 : RuntimeException 안던지면 롤백안됨 
			throw new RuntimeException("공고등록 중 예외 발생", e); 
		}
  } //end uploadJobPost()
  
  /**
   * 공고 수정하기 (공고수정 -> 기존 기술스택 리스트 삭제 -> 기술스택 새로 등록)
   */
  @Transactional
  public void updateJobPost(JobPostingDTO jpDTO) {
  	
  	try {
  		jpm.modifyJobPost(jpDTO); //공고 수정
  		
  		jptm.deleteJobPostingTechStack(jpDTO.getJobPostingSeq()); //기존 기술스택 데이터 모두 삭제 
  		
      for(Integer techStackSeq : jpDTO.getTechStackSeqList()) {
      	jptm.insertjobPostingTechStack(jpDTO.getJobPostingSeq(), techStackSeq); //기술스택 새로 등록 
      }
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("공고 수정중 예외 발생!", e);
		}
  	
  }
  
  
  
  /**
   * 사업자등록번호로 회사정보 가져오기 
   * @param corpNo
   * @return
   */
  public CorpDTO getCorpDTO(long corpNo) {
  	//@ControllerAdvice 로 NotFoundException을 제어중
  	CorpEntity cEntity = cRepository.findById(corpNo).orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));
  	
  	//Entity -> DTO 변환
  	CorpDTO cDTO = CorpDTO.from(cEntity);
  	//태그랑 사업자번호 안담았어. 태그는 필요하긴한데, 그건 조회할때 편하려고 하는건데 연수가 잘 할거니까 생략함
  	return cDTO;
  } //end getCorpDTO()
  

  /**
   * 키워드로 포지션 키워드 리스트 조회
   */
  public List<PositionCodeDTO> pDTOList(String keyword){
  	if(keyword == null || keyword.trim().isEmpty()) {
  		return Collections.emptyList(); //빈 리스트 반환, 이게 뭐지?
  	}
  	
  	List<PositionCodeDTO> pDTOList = new ArrayList<PositionCodeDTO>();
  	
  	pDTOList = jpm.selectPostionList(keyword);
  	
  	return pDTOList;
  }
  
  /**
   * 기술스택 키워드 리스트 조회 
   */
  public List<TechStackDTO> tsDTO(String keyword){
  	if(keyword == null || keyword.trim().isEmpty()) {
  		return Collections.emptyList(); //빈 리스트 반환, 이게 뭐지?
  	}
  	
  	List<TechStackDTO> tsDTOList = new ArrayList<TechStackDTO>();
  	
  	tsDTOList = jpm.selectTechStackList(keyword);
  	
  	return tsDTOList;
  }
  
  /**
   * 나의 공고 전체,진행중,마감 갯수 가져오기 
   */
  public List<Map<String, Integer>> selectMyJobPostingCnt(Long corpNo){
  	
  	List<Map<String, Integer>> postCntList = jpm.selectMyPostingCount(corpNo);
  	
  	return postCntList;
  }
  /**
   * 나의 공고 리스트 가져오기 
   */
  public List<JobPostingDTO> selectMyJobPosting(JobPostingDTO jpDTO){
  	
  	List<JobPostingDTO> jpList = new ArrayList<JobPostingDTO>();
  	jpList = jpm.selectMyAllPosting(jpDTO);
  	
  	return jpList;
  }
  
  /**
   * 공고 강제(조기)마감 처리 
   */
  public void updateJobPotingToEnd(int jobPostingSeq) {
  	jpm.finishJobPosting(jobPostingSeq);
  	
  }
  
  /**
   * 나의 특정 공고 가져오기
   */
  public JobPostingDTO selectMyJobPostingOne(long corpNo, int jobPostingSeq) {
  	
  	JobPostingEntity jpEntity  = jRepository.findByCorpNo_CorpNoAndJobPostingSeq(corpNo, jobPostingSeq);
  	
  	JobPostingDTO jpDTO = JobPostingDTO.from(jpEntity);
  	
  	return jpDTO;
  }
  
  /**
   * 공고번호로 해당 공고의 지원자들의 통계자료 가져오기 
   */
  public JobPostingApplicantStatsDTO selectApplicantStats(int JobPostingSeq) {
  	
  	try {
  		JobPostingApplicantStatsDTO jpasDTO = jpm.selectApplicantStats(JobPostingSeq);
  		//System.out.println("[디버깅] 서비_ selectApplicantStats : " + jpasDTO);
  		return jpasDTO;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("지원자 통계 조회 중 오류가 발생했습니다.", e);
		}
  	
  }
  
  /**
   * 공고 번호로 해당 공고 정보 가져오기 
   */
  public JobPostingDTO selectJobPostingInfo(int JobPostingSeq) {
  	JobPostingEntity jpEntity = jRepository.findById(JobPostingSeq).orElseThrow(() -> new NotFoundException("해당 공고가 존재하지 않습니다."));
  	JobPostingDTO jpDTO = JobPostingDTO.from(jpEntity);
  	
  	return jpDTO;
  }
  
  
}
