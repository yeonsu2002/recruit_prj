package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.ResumeDetailDTO;
import kr.co.sist.user.dto.*;

@Mapper
public interface ResumeDetailMapper {
  
  ResumeDetailDTO selectResumeBasic(Long resumeSeq);

  List<PositionCodeDTO> selectPositionsByResume(Long resumeSeq);

  List<TechStackDTO> selectTechStacksByResume(Long resumeSeq);

  List<EducationHistoryDTO> selectEducationsByResume(Long resumeSeq);

  List<CareerDTO> selectCareersByResume(Long resumeSeq);

  List<ProjectDTO> selectProjectsByResume(Long resumeSeq);

  List<AdditionalInfoDTO> selectAdditionalsByResume(Long resumeSeq);

  List<SelfIntroductionDTO> selectIntroductionsByResume(Long resumeSeq);

  LinkDTO selectLinksByResume(Long resumeSeq);
}
