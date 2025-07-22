package kr.co.sist.user.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.ApplicantStatisticsDTO;
import kr.co.sist.user.dto.MyApplicantDTO;
import kr.co.sist.user.dto.MyApplicantSearchDTO;
import kr.co.sist.user.dto.MyPostingDTO;
import kr.co.sist.user.mapper.MessageMapper;
import kr.co.sist.user.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final MyPageMapper mpMapper;
	private final MessageMapper messageMapper;

	// 내가 스크랩한 공고 가져오기
	public List<MyPostingDTO> searchMyScrapPosting(String email){

		return mpMapper.selectMyScrapPosting(email);
	}
	
	// 내가 스크랩한 공고 개수
	public int cntMyScrapPosting(String email) {
		
		return mpMapper.cntMyScrapPosting(email);
	}
	
	// 내 모든 지원 목록
	public List<MyApplicantDTO> searchMyAllApplicant(String email) {
		return mpMapper.selectMyApplicant(email);
	}

	// 내 지원 목록
	public List<MyApplicantDTO> searchMyApplicant(MyApplicantSearchDTO searchDTO) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<MyApplicantDTO> applicants = mpMapper.selectMyAllApplicant(searchDTO);
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
	}// searchMyApplicant

	// 지원 취소
	public int cancelApplication(int jobApplicatioinSeq) {

		return mpMapper.updateApplicationCancel(jobApplicatioinSeq);

	}

	// 지원 내역 삭제
	public int removeApplication(int jobApplicationSeq) {

		return mpMapper.deleteApplication(jobApplicationSeq);
	}

	// 지원자 통계 가공
	public ApplicantStatisticsDTO getApplicantStatistics(List<MyApplicantDTO> applicantDTO) {

		ApplicantStatisticsDTO statistics = new ApplicantStatisticsDTO();

		// 모든 내 지원 내역을 돌며 각각의 상태 수 설정
		for (MyApplicantDTO dto : applicantDTO) {

			switch (dto.getPassStage()) {
			case 1:
				statistics.setDocPassed(statistics.getDocPassed() + 1);
				break;
			case 2:
				statistics.setPassed(statistics.getPassed() + 1);
				break;
			case 3:
				statistics.setFailed(statistics.getFailed() + 1);
				break;
			}

			statistics.setCompleted(statistics.getCompleted() + 1);
		}
		return statistics;

	}// getApplicantStatistics

}// class
