package kr.co.sist.corp.service;

import java.util.List;

import kr.co.sist.corp.dto.InterviewOfferDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;

public interface TalentPoolService {
	List<TalentPoolDTO> getAllTalents(Long corpNo);
	
	String scrapResume(Long resumeSeq, Long corpNo);
	
	List<TalentPoolDTO> getScrappedTalents(Long corpNo);
	
	//전체 인재 총 건수
	int selectTalentTotalCount();
	
	//전체 인재
	 List<TalentPoolDTO> getPaginatedTalents(String sortBy, String order, int offset, int size, Long corpNo);
	  
	//스크랩 인재 리스트
  List<TalentPoolDTO> getScrappedTalents(Long corpNo, int offset, int size);
  //스크랩 인재 총 건수
  int getScrappedTalentsCount(Long corpNo);
  
  //면접 제안
  void sendInterviewProposal(InterviewOfferDTO proposalDto);
  
  //이력서 상세
  TalentPoolDTO selectResumeDetail(int resumeNo);
  
  //이력서 열람
	void viewResume(Long resumeSeq, Long corpNo);


} 
