package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.ResumeDataDTO;
import kr.co.sist.user.entity.LinkEntity;
import kr.co.sist.user.entity.ResumeEntity;
import kr.co.sist.user.entity.ResumePositionCodeEntity;
import kr.co.sist.user.entity.ResumeTechStackEntity;
import kr.co.sist.user.mapper.ResumeMapper;
import kr.co.sist.user.repository.LinkRepository;
import kr.co.sist.user.repository.ResumePositionCodeRepository;
import kr.co.sist.user.repository.ResumeRepository;
import kr.co.sist.user.repository.ResumeTechStackRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeMapper rm;
	
	private final ResumeRepository rr;
	private final ResumePositionCodeRepository rpcr;
	private final ResumeTechStackRepository rtsr;
	private final LinkRepository lr;
	
	private final PositionCodeService pcs;
	
	public List<PositionCodeDTO> searchAllPositionCode(){
		return rm.selectAllPositionCode();
	}
	
	/**
	 * 이력서 생성
	 * @param rdd
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Transactional
	public int addModifyResume(ResumeDataDTO rdd) throws IllegalArgumentException {
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); //현재 날짜 초 까지만 저장
		
		//이력서 레코드 생성
		ResumeEntity re = rdd.getBasicInfo();
		
		re.setCreatedAt(now.toString());
		re.setEmail("wngustjr1306");
		re.setIsPublic(Boolean.parseBoolean(re.getIsPublic()) ? "Y" : "N"); //이력서 공개 여부 처리
		re.setCareerType(rdd.getCareers() != null ? "E" : "N"); //경력 여부 처리
		
		int resumeSeq = rr.save(re).getResumeSeq(); //이력서 레코드 생성 후, pk 반환
		
		//이력서-포지션코드 레코드 생성
		List<Integer> positions = rdd.getPositions();
		for(int position : positions) {
			ResumePositionCodeEntity rpce = new ResumePositionCodeEntity();
			rpce.setResumeSeq(resumeSeq);
			rpce.setPositionSeq(position);
			rpcr.save(rpce);
		}
		
		//이력서-기술스택 레코드 생성
		List<Integer> skills = rdd.getSkills();
		for(int skill : skills) {
			ResumeTechStackEntity rtse = new ResumeTechStackEntity();
			rtse.setResumeSeq(resumeSeq);
			rtse.setTechStackSeq(skill);
			rtsr.save(rtse);
		}
		
		//링크 레코드 생성
		LinkEntity le = rdd.getLinks();
		le.setResumeSeq(resumeSeq);
		lr.save(le); 
		
		//학력 레코드 생성
		
		
		return resumeSeq;
	}
}
