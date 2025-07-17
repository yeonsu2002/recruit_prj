package kr.co.sist.corp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.ApplicantDTO;
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
	 * @param corpNo
	 * @return
	 */
	public List<ApplicantDTO> searchAllApplicant(long corpNo) {
		
		List<ApplicantDTO> applicantDTO = applicantMapper.selectAllApplicant(corpNo);
		
		for(ApplicantDTO applicant : applicantDTO) {
			
			//이름 복호화
			if(applicant.getName() != null) {
				applicant.setName(cu.decryptText(applicant.getName()));
			}
			
			//경력 설정
			applicant.setCareerType((applicant.getCareerType() == "N") ? "신입" : "경력");
			
			//합격 상태 설정
			String passStage = switch(applicant.getPassStage()) {
				case 0 -> "신규지원"; case 1 -> "서류통과"; case 2 -> "최종합격"; case 3 -> "불합격"; default -> "미정";
			};
			applicant.setStageName(passStage);
			
			//지원 상태 설정
			String applicationStatus = switch(applicant.getApplicationStatus()) {
				case 0 -> "미열람"; case 1 -> "열람"; case 2 -> "지원취소"; default -> "미정";
			};
			applicant.setStatusName(applicationStatus);
			
		}
		
		
		
		
		
			
		return applicantDTO;
	}
}
