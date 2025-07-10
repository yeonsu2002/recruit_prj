package kr.co.sist.corp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.corp.mapper.JobPostingMapper;

@Service
public class JobPostingCorpService {

  private final JobPostingMapper jpm;
  
  public JobPostingCorpService(JobPostingMapper jpm) {
    this.jpm = jpm;
  }
  
  /**
   * 연속적인 insert는 항상 서비스단에서 묶어서 처리하고, @Transactional을 걸어야 안전빵
   * @param jpDTO
   * @return
   */
  @Transactional 
  public void uploadJobPost(JobPostingDTO jpDTO) {
    //1. 공고등록
    int result = jpm.insertJobPost(jpDTO);
    
    /**
     * jpDTO.getJobPostingSeq() 왜 돼?
     * jpDTO는 참조변수
     * mapper내부에서 setter(.setJobPostingSeq(123)) 이런 setter를 호출했꼬, 
     *  (keyProperty="jobPostingSeq" => DTO의 해당 필드에 자동 주입)
     * 나는 그 참조변수를다시 가져와서 보는 것이니 매개변수때와 그 값이 다르다. 
     */
    int jobPostingSeq = jpDTO.getJobPostingSeq();
    
    //2. 기술 스택등록(중간테이블)
    for(Integer skillSeq : jpDTO.getTechStackSeqList()) {
      // 무슨mapper.insertjobPostingTechStack(jobPostingSeq, skillSeq);
    }
  }
  
}
