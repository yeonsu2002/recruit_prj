package kr.co.sist.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.user.entity.ProjectEntity;
import kr.co.sist.user.entity.ProjectTechStackEntity;
import kr.co.sist.user.repository.ProjectRepository;
import kr.co.sist.user.repository.ProjectTechStackRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository pr;
	private final ProjectTechStackRepository ptsr;
	
	/**
	 * 프로젝트 레코드와 프로젝트별 기술 레코드를 생성
	 * @param resumeSeq
	 * @param projects
	 * @param projectSkills
	 */
	@Transactional
	public void addProjectAndSkills(int resumeSeq, List<ProjectEntity> projects, List<List<Integer>> projectSkills) {
		
		for(int i=0; i<projects.size(); i++) {
			ProjectEntity project = projects.get(i);
			List<Integer> skills = projectSkills.get(i);
			
			project.setResumeSeq(resumeSeq); //이력서seq 설정
			project.setReleaseStatus(Boolean.parseBoolean(project.getReleaseStatus()) ? "Y" : "N"); //배포상태 설정
			int pojectSeq = pr.save(project).getProjectSeq(); //프로젝트 레코드 생성 후, seq 반환
		
			//각 프로젝트별 스킬들을 프로젝트-스킬 레코드에 저장
			for(int skill : skills) {
				ProjectTechStackEntity projectSkill = new ProjectTechStackEntity();
				projectSkill.setProjectSeq(pojectSeq);
				projectSkill.setTechStackSeq(skill);
				ptsr.save(projectSkill);
			}
		}
	}
}
