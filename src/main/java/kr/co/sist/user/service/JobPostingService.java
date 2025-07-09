package kr.co.sist.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.mapper.JobPostingMapper;


@Service
public class JobPostingService {

    @Autowired
    private JobPostingMapper jpm;

    public List<JobPostDTO> getJobPostings(Integer jobPostingSeq) {
        if (jobPostingSeq == null) {
            // 로그 추가
            System.out.println("전체 공고 조회 호출");
            return jpm.selectAllJobPostings(); // 전체 공고 조회
        } else {
            // 로그 추가
            System.out.println("특정 공고 조회 호출, jobPostingSeq: " + jobPostingSeq);
            return jpm.selectJobPostingsBySeq(jobPostingSeq); // 특정 공고 조회
        }
    }
}
