package kr.co.sist.corp.service;

import java.util.List;

import kr.co.sist.corp.dto.TalentPoolDTO;

public interface TalentPoolService {
	List<TalentPoolDTO> getAllTalents(Long corpNo);

	int selectTalentTotalCount();
	
	String scrapResume(Long resumeSeq, Long corpNo);
	
	List<TalentPoolDTO> getScrappedTalents(Long corpNo);
	
	List<TalentPoolDTO> getPaginatedTalents(int offset, int size, Long corpNo);

//	List<TalentPoolDTO> getPaginatedScrappedTalents(int offset, int size, Long corpNo);

//	int selectScrappedTalentTotalCount(Long corpNo);
	


} 
