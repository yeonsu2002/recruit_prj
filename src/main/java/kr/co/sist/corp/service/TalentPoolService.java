package kr.co.sist.corp.service;

import java.util.List;

import kr.co.sist.corp.dto.InterviewOfferDTO;
import kr.co.sist.corp.dto.RecentlyViewedDTO;
import kr.co.sist.corp.dto.ResumeDetailDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;

public interface TalentPoolService {
	List<TalentPoolDTO> getAllTalents(Long corpNo);
	
	String scrapResume(Long resumeSeq, Long corpNo);
	
//	List<TalentPoolDTO> getScrappedTalents(Long corpNo);
	
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
  void sendInterviewOffer(InterviewOfferDTO ioDTO);
  //기업 정보 
  InterviewOfferDTO getCorpInfoByCorpNo(Long corpNo);
  
  //이력서 상세
  TalentPoolDTO selectResumeDetail(int resumeSeq);
  //이력서 열람
	void viewResume(Long resumeSeq, Long corpNo);
	
  
  // 최근 열람한 이력서 목록 조회
	// 이력서 상세 정보 조회
  List<Integer> getRecentlyViewedResumes(Long corpNo, int startRow, int endRow);
  List<TalentPoolDTO> getResumeDetailsBySeqs(List<Integer> resumeSeqs);
  // 최근 열람한 이력서 총 건수 조회
  int getRecentlyViewedResumesCount(Long corpNo);



} 
