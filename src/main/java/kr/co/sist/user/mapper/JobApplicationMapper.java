package kr.co.sist.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.user.dto.AttachmentDTO;
import kr.co.sist.user.dto.JobApplicationDTO;
import kr.co.sist.user.dto.ResumeDTO;

@Mapper
public interface JobApplicationMapper {

	 // 1. 이메일로 이력서 조회
  List<ResumeDTO> selectResumesByEmail(@Param("email") String email);

  // 2. (기존 방식) 공고 지원 등록 (Map 기반)
  void insertJobApplication(Map<String, Object> paramMap);

  // 3. (DTO 방식) 공고 지원 등록
  void insertJobApplicationVO(JobApplicationDTO applicationDTO);

	//Resume 첨부파일 조회
	//List<AttachmentDTO> selectAttachmentsByResumeSeq(Integer resumeSeq);
	
	//지원-첨부파일 관계 저장
	//void insertApplicationAttachment(Integer attachmentSeq, Integer applicationSeq);

}
