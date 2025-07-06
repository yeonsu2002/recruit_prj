package kr.co.sist.user.dto;

import java.util.List;

import kr.co.sist.user.entity.AdditionalInfoEntity;
import kr.co.sist.user.entity.CareerEntity;
import kr.co.sist.user.entity.EducationHistoryEntity;
import kr.co.sist.user.entity.LinkEntity;
import kr.co.sist.user.entity.ProjectEntity;
import kr.co.sist.user.entity.ResumeEntity;
import kr.co.sist.user.entity.SelfIntroductionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResumeDataDTO {
	
	private ResumeEntity basicInfo;
	private LinkEntity links;
	private List<String> positions;
	private List<String> skills;
	private List<EducationHistoryEntity> educations;
	private List<CareerEntity> careers;
	private List<ProjectEntity> projects;
	private List<AdditionalInfoEntity> etc;
	private List<SelfIntroductionEntity> introductions;
}
