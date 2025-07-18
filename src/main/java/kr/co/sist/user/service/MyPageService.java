package kr.co.sist.user.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.ApplicantStatisticsDTO;
import kr.co.sist.user.dto.JobPostingScrapDTO;
import kr.co.sist.user.dto.MyApplicantDTO;
import kr.co.sist.user.mapper.MessageMapper;
import kr.co.sist.user.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final MyPageMapper mpMapper;
	private final MessageMapper messageMapper;

	public List<JobPostingScrapDTO> searchScrapPosting(String email) {
		return mpMapper.selectScrapPosting(email);
	}

	// 내 지원 목록
	public List<MyApplicantDTO> searchMyApplicant(String email) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<MyApplicantDTO> applicants = mpMapper.selectMyApplicant(email);

		for (MyApplicantDTO applicant : applicants) {

			// 디데이 추가
			LocalDate endDate = LocalDate.parse(applicant.getPostingEndDt(), formatter);
			long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);

			String dday;
			if (days == 0) {
				dday = "D-day";
			} else if (days > 0) {
				dday = "D-" + days;
			} else {
				dday = "마감";
			}
			applicant.setDDay(dday);

			// 날짜 가공
			applicant.setApplicationDate(applicant.getApplicationDate().substring(0, 10));

		}

		return applicants;
	}//searchMyApplicant
	
	//지원 취소
	public int cancelApplication(int jobApplicatioinSeq) {
		
		return mpMapper.updateApplicationCancel(jobApplicatioinSeq);
		
	}
	
	//지원자 통계 가공
	public ApplicantStatisticsDTO getApplicantStatistics(List<MyApplicantDTO> applicantDTO) {
		
		ApplicantStatisticsDTO statistics = new ApplicantStatisticsDTO();
		
		//모든 내 지원 내역을 돌며 각각의 상태 수 설정
		for (MyApplicantDTO dto : applicantDTO) {
      if (dto.getApplicationStatus() == 2) continue; // 지원취소는 제외

      switch (dto.getPassStage()) {
          case 0: statistics.setCompleted(statistics.getCompleted()+1); break;
          case 1: statistics.setDocPassed(statistics.getDocPassed()+1); break;
          case 2: statistics.setPassed(statistics.getPassed()+1); break;
          case 3: statistics.setFailed(statistics.getFailed()+1); break;
      }
  }
		
		return statistics;
	}//statistics.setCompleted(statistics.getCompleted()+1)
	

}//class
