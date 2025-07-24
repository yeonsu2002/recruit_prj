package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.AttachmentDTO;
import kr.co.sist.user.dto.JobApplicationDTO;
import kr.co.sist.user.dto.ResumeDTO;
import kr.co.sist.user.mapper.JobApplicationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationMapper jobApplicationMapper;

    public List<ResumeDTO> getResumesByEmail(String email) {
        return jobApplicationMapper.selectResumesByEmail(email);
    }
    
    
    /**
     * 사용자 이메일로 모든 첨부파일 목록 조회
     */
    public List<AttachmentDTO> getAttachmentsByEmail(String email) {
        return jobApplicationMapper.selectAttachmentsByEmail(email);
    }

    //첨부파일 x
    public void applyToJob(String email, Integer resumeSeq, Integer jobPostingSeq) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("email", email);
        paramMap.put("resumeSeq", resumeSeq);
        paramMap.put("jobPostingSeq", jobPostingSeq);

        jobApplicationMapper.insertJobApplication(paramMap);
    }
    
    
    //첨부파일O
    public void applyToJobUsingResumeAttachments(Integer resumeSeq, Integer jobPostingSeq, String email) {
      // 1. 지원정보 등록
      JobApplicationDTO applicationDTO = new JobApplicationDTO();
      applicationDTO.setResumeSeq(resumeSeq);
      applicationDTO.setJobPostingSeq(jobPostingSeq);
      applicationDTO.setApplicationStatus(0);
      applicationDTO.setApplicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      applicationDTO.setIsRead("N");
      applicationDTO.setPassStage(0);
      jobApplicationMapper.insertJobApplicationVO(applicationDTO);
      Integer applicationSeq=applicationDTO.getJobApplicationSeq();


      List<AttachmentDTO> userAttachments=jobApplicationMapper.selectAttachmentsByEmail(email);
      
      for(AttachmentDTO attachment : userAttachments) {
      	jobApplicationMapper.insertApplicationAttachment(attachment.getAttachmentSeq(), applicationSeq);

      }
			
  }
    
    public void applyToJobWithSelectedAttachments(Integer resumeSeq, Integer jobPostingSeq, List<Integer> selectedAttachments, String email) {
      // 1. 지원정보 등록
      JobApplicationDTO applicationDTO = new JobApplicationDTO();
      applicationDTO.setResumeSeq(resumeSeq);
      applicationDTO.setJobPostingSeq(jobPostingSeq);
      applicationDTO.setApplicationStatus(0);
      applicationDTO.setApplicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      applicationDTO.setIsRead("N");
      applicationDTO.setPassStage(0);
      jobApplicationMapper.insertJobApplicationVO(applicationDTO);
      
      Integer applicationSeq = applicationDTO.getJobApplicationSeq();
      
      // 2. 선택된 첨부파일들만 등록
      if (selectedAttachments != null && !selectedAttachments.isEmpty()) {
          for (Integer attachmentSeq : selectedAttachments) {
              jobApplicationMapper.insertApplicationAttachment(attachmentSeq, applicationSeq);
          }
      }
  }


	
}