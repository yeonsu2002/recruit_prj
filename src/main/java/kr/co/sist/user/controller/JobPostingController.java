package kr.co.sist.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.dto.ResumeDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.JobPostingService;
import kr.co.sist.user.service.ResumeService;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // lombok으로 final 필드 자동 생성자 주입
public class JobPostingController {
    
    private final JobPostingService jps;
    private final UserRepository ur;
    private final CipherUtil cu;
   private final  ResumeService rs;

    
    //개발자채용
    @GetMapping("/user/job_posting/job_posting")
    public String getJobPostings(@RequestParam(required = false) Integer jobPostingSeq, Model model) {
        List<JobPostDTO> jobPostingList = jps.getJobPostings(jobPostingSeq);
        model.addAttribute("jobPostingList", jobPostingList);
        return "user/job_posting/job_posting";
    }

    //공고상세보기
    @GetMapping("/user/job_posting/job_posting_detail")
    public String JobPostingDetailPage(@RequestParam(required = false) Integer jobPostingSeq,
                                       @AuthenticationPrincipal CustomUser userInfo,
                                       Model model) {
        JobPostDTO jDto = jps.findById(jobPostingSeq);
        model.addAttribute("jDto", jDto);
        System.out.println("Job Posting Detail: " + jDto.getTechStacks());

        if (userInfo != null) {
            UserEntity userEntity = ur.findById(userInfo.getEmail()).orElse(null);
            if (userEntity != null) {
                try {
                    // 유저 정보 복호화
                    userEntity.setPhone(cu.plainText(userEntity.getPhone()));
                    String birth = userEntity.getBirth();
                } catch (Exception e) {
                    // 복호화 실패 시 처리
                    userEntity.setPhone("복호화 실패");
                    userEntity.setBirth("복호화 실패");
                }
                // 복호화된 데이터를 UserDTO에 담아서 전달
                UserDTO user = new UserDTO(userEntity);
                model.addAttribute("user", user);
                
                List<ResumeDTO> resumes = rs.searchAllResumeByUser(userInfo.getEmail());
                model.addAttribute("resumes", resumes);
            }
        }

        return "user/job_posting/job_posting_detail";
    }
    
    
    //기업정보
    @GetMapping("/user/job_posting/company_info")
    public String companyInfo() {
        return "user/job_posting/company_info";
    }
    
    
    //기업리뷰
    @GetMapping("/user/job_posting/review")
    public String review() {
        return "user/job_posting/review";
    }
}
