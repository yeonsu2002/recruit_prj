package kr.co.sist.corp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.ApplicantDTO;
import kr.co.sist.corp.dto.ApplicantSearchDTO;
import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.corp.mapper.ApplicantMapper;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantMapper applicantMapper;

	private final CipherUtil cu;

	/**
	 * 해당 회사의 모든 지원자 찾기
	 * 
	 * @param corpNo
	 * @return
	 */
	public List<ApplicantDTO> searchAllApplicant(long corpNo) {

		List<ApplicantDTO> applicantDTO = applicantMapper.selectAllApplicant(corpNo);

		return manufactureApplicantDTO(applicantDTO);
	}// searchAllApplicant

	/**
	 * 조건에 맞는 지원자 찾기
	 * 
	 * @param corpNo
	 * @return
	 */
	public List<ApplicantDTO> searchApplicant(ApplicantSearchDTO searchDTO) {

		List<ApplicantDTO> applicantDTO = applicantMapper.selectApplicant(searchDTO);
		
		return manufactureApplicantDTO(applicantDTO);
	}

	/**
	 * 모든 진행중인 공고
	 * 
	 * @param corpNo
	 * @return
	 */
	public List<JobPostingDTO> searchPostingProgress(long corpNo) {

		return applicantMapper.selectPostingProgress(corpNo);

	}// searchPostingProgress

	/**
	 * 모든 마감된 공고
	 * 
	 * @param corpNo
	 * @return
	 */
	public List<JobPostingDTO> searchPostingClosed(long corpNo) {

		return applicantMapper.selectPostingClosed(corpNo);

	}// searchPostingClosed

	/**
	 * 모든 공고
	 * 
	 * @param corpNo
	 * @return
	 */
	public List<JobPostingDTO> searchPostingAll(long corpNo) {

		return applicantMapper.selectPostingAll(corpNo);

	}// searchPostingClosed

	/**
	 * applicantDTO 가공해서 반환
	 * @param applicantDTO
	 * @return
	 */
	public List<ApplicantDTO> manufactureApplicantDTO(List<ApplicantDTO> applicantDTO) {

		for (ApplicantDTO applicant : applicantDTO) {

			// 경력 설정
			applicant.setCareerType((applicant.getCareerType().equals("N")) ? "신입" : "경력");
			// 합격 상태 설정
			String passStage = switch (applicant.getPassStage()) {
			case 0 -> "신규지원";
			case 1 -> "서류통과";
			case 2 -> "최종합격";
			case 3 -> "불합격";
			default -> "미정";
			};
			applicant.setStageName(passStage);

			// 지원 상태 설정
			String applicationStatus = switch (applicant.getApplicationStatus()) {
			case 0 -> "미열람";
			case 1 -> "열람";
			case 2 -> "지원취소";
			default -> "미정";
			};
			applicant.setStatusName(applicationStatus);

		}
		
		return applicantDTO;

	}//manufactureApplicantDTO
	
	public int modifyResumeReadStatus(int resumeSeq) {
		
		return applicantMapper.updateResumeReadStatus(resumeSeq);
	}

}
