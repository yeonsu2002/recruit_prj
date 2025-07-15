package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.corp.dto.ResumeScrapDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;

@Mapper
public interface TalentPoolMapper {
	
	List<TalentPoolDTO> selectAllTalents();

	int selectTalentTotalCount();
	
	int checkScrapExists(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	int insertScrap(ResumeScrapDTO sDTO);

	int updateScrap(ResumeScrapDTO sDTO);
	
	int deleteScrap(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	List<TalentPoolDTO> selectScrappedTalents(@Param("corpNo") Long corpNo);

	int isResumeScrapped(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	List<TalentPoolDTO> selectPaginatedTalents(@Param("offset") int offset, @Param("size") int size);
    
	List<TalentPoolDTO> selectPaginatedScrappedTalents(@Param("offset") int offset, @Param("size") int size, @Param("corpNo") Long corpNo);

    int selectScrappedTalentTotalCount(@Param("corpNo") Long corpNo);
	
}
