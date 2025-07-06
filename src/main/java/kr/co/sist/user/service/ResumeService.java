package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.ResumeDataDTO;
import kr.co.sist.user.entity.ResumeEntity;
import kr.co.sist.user.mapper.ResumeMapper;
import kr.co.sist.user.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeMapper rm;
	private final ResumeRepository rr;
	
	public List<PositionCodeDTO> searchAllPositionCode(){
		return rm.selectAllPositionCode();
	}
	
	public int addModifyResume(ResumeDataDTO rdd) throws IllegalArgumentException {
		
		ResumeEntity re = rdd.getBasicInfo();
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);;
		re.setCreatedAt(now.toString());
		re.setEmail("wngustjr1306");
		
		if(re.getIsPublic().equals("true")) {
			re.setIsPublic("Y");
		} else {
			re.setIsPublic("N");
		}
		
		int resumeSeq = rr.save(re).getResumeSeq();
		return resumeSeq;
	}
}
