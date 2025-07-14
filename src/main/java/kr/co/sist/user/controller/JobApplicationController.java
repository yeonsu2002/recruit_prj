package kr.co.sist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.user.dto.JobApplicationDTO;
import kr.co.sist.user.dto.UserDTO;  // 사용자 정보 DTO (로그인한 사용자)
import kr.co.sist.user.service.JobApplicationService;
import kr.co.sist.user.service.JobPostingService;

@Controller
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobPostingService jobPostingService;
    
    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, JobPostingService jobPostingService) {
        this.jobApplicationService = jobApplicationService;
        this.jobPostingService = jobPostingService;
    }

    /**
     * 공고 상세 페이지로 이동하여 지원하기 폼을 제공
     */
    @GetMapping("/user/job_application/job_apply")
    public String applyForJob(@RequestParam Integer jobPostingSeq, HttpSession session, Model model) {
		/*
		 * // 세션에서 로그인된 사용자 확인 UserDTO loggedInUser = (UserDTO)
		 * session.getAttribute("loggedInUser");
		 * 
		 * // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트 if (loggedInUser == null) { return
		 * "redirect:/login"; // 로그인 페이지로 리다이렉트 }
		 */
    	
    	
    	    UserDTO user = new UserDTO();
    	    user.setEmail("testuser@domain.com");
    	    user.setName("테스트 사용자");
    	    user.setPhone("010-1234-5678");

    	    // 사용자 정보를 모델에 담아서 job_apply.html로 전달
    	    model.addAttribute("user", user);
    	    model.addAttribute("jobPosting", jobPostingService.findById(jobPostingSeq));

    	    return "user/job_application/job_apply";  // job_apply.html로 이동
    	}


    /**
     * 지원하기 처리 (폼에서 지원 버튼 클릭 후)
     */
    @PostMapping("/user/job_application/job_apply")
    public String applyJob(@RequestParam Integer jobPostingSeq,
                           @RequestParam Integer resumeSeq,   // 선택한 이력서 번호
                           @RequestParam String applicationStatus, // 지원 상태
                           @RequestParam(required = false) String interviewDate, // 면접 날짜 (옵션)
                           Model model) {

        // 로그인 없이 임시 사용자 정보 설정
        UserDTO user = new UserDTO();
        user.setEmail("testuser@domain.com");
        user.setName("테스트 사용자");
        user.setPhone("010-1234-5678");

        // JobApplicationDTO 객체 생성 및 값 설정
        JobApplicationDTO jobApplicationDTO = new JobApplicationDTO();
        jobApplicationDTO.setJobPostingSeq(jobPostingSeq);  // 공고 시퀀스
        jobApplicationDTO.setResumeSeq(resumeSeq);          // 선택한 이력서 시퀀스
        jobApplicationDTO.setApplicationStatus(applicationStatus);  // 지원 상태 (지원완료 등)

        // 지원 날짜는 현재 날짜로 설정
        String applicationDate = java.time.LocalDate.now().toString();  // LocalDate로 현재 날짜 구하기
        jobApplicationDTO.setApplicationDate(applicationDate);

        // 면접 날짜가 입력되지 않았다면 null 처리
        jobApplicationDTO.setInterviewDate(interviewDate != null ? interviewDate : "");

        // 읽지 않음 상태로 설정 (기본값 "N")
        jobApplicationDTO.setIsRead("N");

        // DB에 지원 정보 저장
        jobApplicationService.applyForJob(jobApplicationDTO);

        // 사용자에게 성공 메시지와 함께 공고 목록을 다시 보여줌
        model.addAttribute("message", "지원이 완료되었습니다!");

        // 공고 목록 페이지로 리다이렉트
        return "redirect:/user/job_posting/job_posting";  // 공고 목록으로 리다이렉트
    }
}
