package kr.co.sist.user.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
        // 중복 지원 여부 확인
        JobApplicationDTO existing = jobApplicationMapper.selectExistingApplication(resumeSeq, jobPostingSeq);
        if (existing != null && (existing.getApplicationStatus() == null || existing.getApplicationStatus() != 2)) {
            throw new IllegalStateException("이미 해당 공고에 지원하셨습니다.");
        }
        
        JobApplicationDTO jDTO = new JobApplicationDTO();
        jDTO.setResumeSeq(resumeSeq);
        jDTO.setJobPostingSeq(jobPostingSeq);
        jDTO.setApplicationStatus(0);
        jDTO.setApplicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        jDTO.setIsRead("N");
        jDTO.setPassStage(0);
        
        jobApplicationMapper.insertJobApplicationVO(jDTO);
        Integer jobApplicationSeq = jDTO.getJobApplicationSeq();
        
    }
    
    // 선택된 첨부파일로 지원
    public void applyToJobWithSelectedAttachments(Integer resumeSeq, Integer jobPostingSeq, String email) {
    	
        // 중복 지원 여부 확인
        JobApplicationDTO existing = jobApplicationMapper.selectExistingApplication(resumeSeq, jobPostingSeq);
        if (existing != null && (existing.getApplicationStatus() == null || existing.getApplicationStatus() != 2)) {
            throw new IllegalStateException("이미 해당 공고에 지원하셨습니다.");
        }
        
        JobApplicationDTO jDTO = new JobApplicationDTO();
        jDTO.setResumeSeq(resumeSeq);
        jDTO.setJobPostingSeq(jobPostingSeq);
        jDTO.setApplicationStatus(0);
        jDTO.setApplicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        jDTO.setIsRead("N");
        jDTO.setPassStage(0);
        
        jobApplicationMapper.insertJobApplicationVO(jDTO);
        Integer jobApplicationSeq = jDTO.getJobApplicationSeq();
        
        
    
    }
}