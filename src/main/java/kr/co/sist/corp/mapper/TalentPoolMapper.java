package kr.co.sist.corp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.corp.dto.InterviewOfferDTO;
<<<<<<< Updated upstream
import kr.co.sist.corp.dto.MessageDTO;
import kr.co.sist.corp.dto.RecentlyViewedDTO;
=======
import kr.co.sist.corp.dto.MailDTO;
>>>>>>> Stashed changes
import kr.co.sist.corp.dto.ResumeScrapDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;

@Mapper
public interface TalentPoolMapper {
	
	List<TalentPoolDTO> selectAllTalents();

	int selectTalentTotalCount();
	
	int checkScrapExists(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	int insertScrap(ResumeScrapDTO sDTO);

	ResumeScrapDTO selectScrap(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);
	
	int updateScrap(ResumeScrapDTO sDTO);
	
	int updateScrapN(ResumeScrapDTO sDTO);
	
	int deleteScrap(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	List<TalentPoolDTO> selectScrappedTalents(@Param("corpNo") Long corpNo);

	int isResumeScrapped(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);

	//전체 인재
//	List<TalentPoolDTO> selectPaginatedTalents(@Param("offset") int offset, @Param("size") int size);
  List<TalentPoolDTO> selectPaginatedTalents(Map<String, Object> paramMap);

    
	List<TalentPoolDTO> selectPaginatedScrappedTalents(@Param("offset") int offset, @Param("size") int size, @Param("corpNo") Long corpNo);

  // 스크랩한 인재 목록 조회 (페이징)
  List<TalentPoolDTO> selectPaginatedScrappedTalents(@Param("corpNo") Long corpNo,
                                              @Param("offset") int offset,
                                              @Param("size") int size);
  // 스크랩한 인재 전체 수
  int countScrappedTalents(@Param("corpNo") Long corpNo);
  //툴바
  List<TalentPoolDTO> getSortedTalentPool(@Param("sortBy") String sortBy,
      @Param("order") String order,
      @Param("offset") int offset,
      @Param("size") int size,
      @Param("corpNo") Long corpNo);
  
<<<<<<< Updated upstream
  void insertInterviewProposal(InterviewOfferDTO ioDTO);
=======
  void insertInterviewProposal(InterviewOfferDTO proposalDto);
>>>>>>> Stashed changes
  
  //이력서 상세
  TalentPoolDTO selectResumeDetail(int resumeNo);
  
  //이력서 열람 확인
  int checkResumeViewExist(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);
  //이력서 열람 기록
  void insertResumeViewLog(@Param("resumeSeq") Long resumeSeq, @Param("corpNo") Long corpNo);
  //이력서 정보
  TalentPoolDTO selectResumeInfo(@Param("resumeSeq") Long resumeSeq);
<<<<<<< Updated upstream
  //기업명
  InterviewOfferDTO getCorpInfoByCorpNo(Long corpNo);

  //메세지(알람) 보내기
  void insertMessage(MessageDTO mail);


  //최근 열람한 이력서 목록 조회
  List<Integer> getRecentlyViewedResumes(Map<String, Object> params);
  // 최근 열람한 이력서 수 조회
  int getRecentlyViewedResumesCount(Long corpNo);
  // 이력서 상세 정보 조회
  List<TalentPoolDTO> selectResumeMemberInfo(Map<String, Object> params);
=======

  
  //메일 알림
  void insertMail(MailDTO mail);

>>>>>>> Stashed changes
  

}
