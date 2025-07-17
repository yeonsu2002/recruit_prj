package kr.co.sist.user.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.JobPostingScrapDTO;
import kr.co.sist.user.dto.MessageDTO;
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
	

}//class
