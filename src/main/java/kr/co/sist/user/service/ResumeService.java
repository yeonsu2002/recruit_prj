package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import kr.co.sist.user.dto.AdditionalInfoDTO;
import kr.co.sist.user.dto.EducationHistoryDTO;
import kr.co.sist.user.dto.LinkDTO;
import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.ProjectDTO;
import kr.co.sist.user.dto.ResumeDataDTO;
import kr.co.sist.user.dto.ResumeTechStackDTO;
import kr.co.sist.user.dto.SelfIntroductionDTO;
import kr.co.sist.user.dto.careerDTO;
import kr.co.sist.user.entity.AdditionalInfoEntity;
import kr.co.sist.user.entity.CareerEntity;
import kr.co.sist.user.entity.EducationHistoryEntity;
import kr.co.sist.user.entity.LinkEntity;
import kr.co.sist.user.entity.ResumeEntity;
import kr.co.sist.user.entity.ResumePositionCodeEntity;
import kr.co.sist.user.entity.ResumeTechStackEntity;
import kr.co.sist.user.entity.SelfIntroductionEntity;
import kr.co.sist.user.mapper.ResumeMapper;
import kr.co.sist.user.repository.AdditionalInfoRepository;
import kr.co.sist.user.repository.CareerRepository;
import kr.co.sist.user.repository.EducationHistoryRepository;
import kr.co.sist.user.repository.LinkRepository;
import kr.co.sist.user.repository.ResumePositionCodeRepository;
import kr.co.sist.user.repository.ResumeRepository;
import kr.co.sist.user.repository.ResumeTechStackRepository;
import kr.co.sist.user.repository.SelfIntroductionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

	//Mapper 생성자주입
	private final ResumeMapper rMapper;
	
	//Repository 생성자주입
	private final ResumeRepository rRepos;
	private final ResumePositionCodeRepository rpcRepos;
	private final ResumeTechStackRepository rtsRepos;
	private final LinkRepository lRepos;
	private final EducationHistoryRepository ehRepos;
	private final CareerRepository cRepos;
	private final AdditionalInfoRepository aiRepos;
	private final SelfIntroductionRepository siRepos;
	
	//Service 생성자주입
	private final PositionCodeService pcServ;
	private final ProjectService pServ;
	
	/**
	 * 이력서 생성
	 * @param rdd
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Transactional
	public boolean addModifyResume(ResumeDataDTO rdd, MultipartFile profileImage) throws IllegalArgumentException {
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); //현재 날짜 초 까지만 저장
		
		//이력서 레코드 생성
		ResumeEntity re = rdd.getBasicInfo();
		
		re.setCreatedAt(now.toString());
		re.setEmail("wngustjr1306");
		if (profileImage != null && !profileImage.isEmpty()) {
			re.setImage(profileImage.getOriginalFilename());//일단 파일명만 저장. 추후에 다시 수정 예정
		} 
		re.setIsPublic(Boolean.parseBoolean(re.getIsPublic()) ? "Y" : "N"); //이력서 공개 여부 처리
		re.setCareerType((rdd.getCareers() != null && !rdd.getCareers().isEmpty()) ? "E" : "N"); //경력 여부 처리
		
		int resumeSeq = rRepos.save(re).getResumeSeq(); //이력서 레코드 생성 후, pk 반환
		
		//이력서-포지션코드 레코드 생성
		List<Integer> positions = rdd.getPositions();
		for(int position : positions) {
			ResumePositionCodeEntity rpce = new ResumePositionCodeEntity();
			rpce.setResumeSeq(resumeSeq);
			rpce.setPositionSeq(position);
			rpcRepos.save(rpce);
		}
		
		//이력서-기술스택 레코드 생성
		List<Integer> skills = rdd.getSkills();
		for(int skill : skills) {
			ResumeTechStackEntity rtse = new ResumeTechStackEntity();
			rtse.setResumeSeq(resumeSeq);
			rtse.setTechStackSeq(skill);
			rtsRepos.save(rtse);
		}
		
		//링크 레코드 생성
		LinkEntity le = rdd.getLinks();
		le.setResumeSeq(resumeSeq);
		lRepos.save(le); 
		
		//학력 레코드 생성
		List<EducationHistoryEntity> educations = rdd.getEducations();
		educations.sort(Comparator.comparing(EducationHistoryEntity::getAdmissionDate)); //입학날짜를 기준으로 정렬
		int indexNum = 1; //순번용 인덱스
		for(EducationHistoryEntity education : educations) {
			education.setResumeSeq(resumeSeq);
			education.setIndexNum(indexNum++);
			ehRepos.save(education);
		}
		
		//경력 레코드 생성
		List<CareerEntity> careers = rdd.getCareers();
		careers.sort(Comparator.comparing(CareerEntity::getStartDate)); //입사날짜를 기준으로 정렬
		indexNum = 1;
		for(CareerEntity career : careers) {
			career.setResumeSeq(resumeSeq);
			career.setIndexNum(indexNum++);
			cRepos.save(career);
		}
		
		//프로젝트 및 프로젝스-기술스택 레코드 생성
		pServ.addProjectAndSkills(resumeSeq, rdd.getProjects(), rdd.getProjectSkills());
		
		//기타사항 레코드 생성
		List<AdditionalInfoEntity> etcs = rdd.getEtc();
		for(AdditionalInfoEntity etc : etcs ) {
			etc.setResumeSeq(resumeSeq);
			aiRepos.save(etc);
		}
		
		//자기소개서 레코드 생성
		List<SelfIntroductionEntity> intros = rdd.getIntroductions();
		for(SelfIntroductionEntity intro : intros) {
			intro.setResumeSeq(resumeSeq);
			siRepos.save(intro);
		}
		
		return true;
	}
	
	/**
	 * seq로 이력서 하나 가져오기
	 * @param resumeSeq
	 * @return
	 */
	public ResumeEntity searchOneResume(int resumeSeq) {
		
		ResumeEntity re = rRepos.findById(resumeSeq).orElse(null);
		
		return re;
	}
	
	/**
	 * seq로 해당 이력서와 연결된 테이블들의 데이터 모두 가져오기
	 * @param resumeSeq
	 * @return
	 */
	public ResumeDataDTO searchOneDetailResume(int resumeSeq) {

		ResumeDataDTO rdDTO = new ResumeDataDTO();
		
		List<PositionCodeDTO> pcDTO = rMapper.selectPositionByResume(resumeSeq);
		LinkDTO lDTO = rMapper.selectLinkByResume(resumeSeq);
		List<ResumeTechStackDTO> rtsDTO = rMapper.selectStackByResume(resumeSeq);
		List<EducationHistoryDTO> ehDTO = rMapper.selectEduByResume(resumeSeq);
		List<careerDTO> cDTO = rMapper.selectCareerByResume(resumeSeq);
		List<ProjectDTO> pDTO = rMapper.selectProjectByResume(resumeSeq);
		List<AdditionalInfoDTO> aiDTO = rMapper.selectEtcByResume(resumeSeq);
		List<SelfIntroductionDTO> siDTO = rMapper.selectIntroByResume(resumeSeq);
		
		//추후 구현
		
		
		return rdDTO;
	}
}
