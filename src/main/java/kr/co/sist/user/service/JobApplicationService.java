package kr.co.sist.user.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
      Integer applicationSeq = applicationDTO.getJobApplicationSeq();

			/*
			 * // 2. 이력서에 첨부된 파일들 조회 List<AttachmentDTO> resumeAttachments =
			 * jobApplicationMapper.selectAttachmentsByResumeSeq(resumeSeq);
			 * 
			 * // 3. 각 첨부파일과 지원정보 관계 등록 for (AttachmentDTO attachment : resumeAttachments) {
			 * jobApplicationMapper.insertApplicationAttachment(attachment.getAttachmentSeq(
			 * ), applicationSeq); }
			 */
  }


	
}