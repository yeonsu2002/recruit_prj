package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.AdditionalInfoDTO;
import kr.co.sist.user.dto.CareerDTO;
import kr.co.sist.user.dto.EducationHistoryDTO;
import kr.co.sist.user.dto.LinkDTO;
import kr.co.sist.user.dto.ResumePositionCodeDTO;
import kr.co.sist.user.dto.ResumeTechStackDTO;
import kr.co.sist.user.dto.SelfIntroductionDTO;

@Mapper
public interface ResumeMapper {

	//이력서 번호로 각 데이터 가져오기
	public List<ResumePositionCodeDTO> selectPositionByResume(int resumeSeq);
	public List<ResumeTechStackDTO> selectStackByResume(int resumeSeq);
	public LinkDTO selectLinkByResume(int resumeSeq);
	public List<EducationHistoryDTO> selectEduByResume(int resumeSeq);
	public List<CareerDTO> selectCareerByResume(int resumeSeq);
	public List<AdditionalInfoDTO> selectEtcByResume(int resumeSeq);
	public List<SelfIntroductionDTO> selectIntroByResume(int resumeSeq);
}
