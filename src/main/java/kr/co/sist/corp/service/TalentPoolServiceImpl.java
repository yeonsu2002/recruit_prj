package kr.co.sist.corp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.corp.dto.InterviewOfferDTO;
import kr.co.sist.corp.dto.MessageDTO;
import kr.co.sist.corp.dto.RecentlyViewedDTO;
import kr.co.sist.corp.dto.ResumeDetailDTO;
import kr.co.sist.corp.dto.ResumeScrapDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.mapper.TalentPoolMapper;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TalentPoolServiceImpl implements TalentPoolService {
	
	@Autowired
	private final TalentPoolMapper talentPoolMapper;
	private final CipherUtil cu;
	
	@Override
	public List<TalentPoolDTO> getAllTalents(Long corpNo) {
	    List<TalentPoolDTO> list = talentPoolMapper.selectAllTalents();

	    for (TalentPoolDTO tDTO : list) {

	        if (tDTO.getFinalEducation() == null || tDTO.getFinalEducation().trim().isEmpty()) {
	            tDTO.setFinalEducation("고등학교 졸업");
	        }

	        tDTO.setTotalCareer(formatCareer(tDTO.getTotalCareer()));

	        // 스크랩 여부 추가!
	        boolean isScrapped = talentPoolMapper.isResumeScrapped(tDTO.getResumeSeq(), corpNo) > 0;
	        tDTO.setIsScrapped(isScrapped ? "Y" : "N");
	    }
	    return list;
	}

	private String formatCareer(String careerStr) {
	    int months = 0;

	    try {
	        months = Integer.parseInt(careerStr);
	    } catch (NumberFormatException e) {
	        // 숫자 변환 안되면 0으로 처리 (신입)
	    }

	    if (months == 0) {
	        return "신입";
	    }

	    int years = months / 12;
	    int remainderMonths = months % 12;

	    String result = "";
	    if (years > 0) {
	        result += years + "년";
	    }
	    if (remainderMonths > 0) {
	        if (years > 0) {
	            result += " ";
	        }
	        result += remainderMonths + "개월";
	    }

	    return result;
	}
	//이력서 총 건수
	@Override
	public int selectTalentTotalCount() {
		return talentPoolMapper.selectTalentTotalCount();
	}//selectTalentTotalCount
	
	//이력서 스크랩
	@Override
	public String scrapResume(Long resumeSeq, Long corpNo) {
	    int exists = talentPoolMapper.checkScrapExists(resumeSeq, corpNo);
	    
	    ResumeScrapDTO sDTO = new ResumeScrapDTO();
	    sDTO.setResumeSeq(resumeSeq);
	    sDTO.setCorpNo(corpNo);

	    int result;
	    if (exists > 0) {
	        result = talentPoolMapper.deleteScrap(resumeSeq, corpNo);
	        return result > 0 ? "scrap_cancel" : "scrap_failed";
	    } else {
	        sDTO.setIsScrapped("Y");
	        result = talentPoolMapper.insertScrap(sDTO);
	        return result > 0 ? "scrap_success" : "scrap_failed";
	    }
	}
	

//전체인재
  @Override
  public List<TalentPoolDTO> getPaginatedTalents(String sortBy, String order, int offset, int size, Long corpNo) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("sortBy", sortBy);
      paramMap.put("order", order);
      paramMap.put("offset", offset);
      paramMap.put("size", size);
      paramMap.put("corpNo", corpNo);

      return talentPoolMapper.selectPaginatedTalents(paramMap);
  }

	//스크랩 인재 리스트
  @Override
  public List<TalentPoolDTO> getScrappedTalents(Long corpNo, int offset, int size) {
      return talentPoolMapper.selectPaginatedScrappedTalents(corpNo, offset, size);
  }
  //스크랩 인재 총 건수
  @Override
  public int getScrappedTalentsCount(Long corpNo) {
      return talentPoolMapper.countScrappedTalents(corpNo);
  }

	//면접 제안
  @Override
  public void sendInterviewProposal(InterviewOfferDTO proposalDto) {
      talentPoolMapper.insertInterviewProposal(proposalDto);  // Mapper 호출
  }
  @Override
  public void sendInterviewOffer(InterviewOfferDTO ioDTO) {
      try {
          // 이력서 번호로 구직자 정보 조회
          TalentPoolDTO talent = talentPoolMapper.selectResumeInfo(ioDTO.getResumeSeq());
          String resumeEmail = talent.getEmail(); // 구직자의 이메일

          // 면접 제안 DTO에 구직자 이메일 설정
          ioDTO.setEmail(resumeEmail);  // 구직자 이메일을 면접 제안 DTO에 설정

          // 메시지 테이블에 면접 제안 정보 저장
          talentPoolMapper.insertInterviewProposal(ioDTO);

      } catch (Exception e) {
          throw new RuntimeException("면접 제안 전송 중 오류 발생", e);
      }
  }

  @Override
  public InterviewOfferDTO getCorpInfoByCorpNo(Long corpNo) {
      // 매퍼에서 정의한 쿼리를 호출하여 데이터를 가져옴
      return talentPoolMapper.getCorpInfoByCorpNo(corpNo);
  }
  
  //이력서 상세
  @Override
  public TalentPoolDTO selectResumeDetail(int resumeNo) {
      TalentPoolDTO dto = talentPoolMapper.selectResumeDetail(resumeNo);

//      if (dto != null && dto.getName() != null) {
//          dto.setName(cu.decryptText(dto.getName()));
//      }
      return dto;
  }

  //이력서 열람
  @Override
  @Transactional
  public void viewResume(Long resumeSeq, Long corpNo) {
      try {
      	
          //이력서열람여부 확인
          int count = talentPoolMapper.checkResumeViewExist(resumeSeq, corpNo);

          if (count == 0) {
              //이력서 열람 기록
              talentPoolMapper.insertResumeViewLog(resumeSeq, corpNo);

              // 이력서 작성자 정보 조회
              TalentPoolDTO resumeOwner = talentPoolMapper.selectResumeInfo(resumeSeq);
              if (resumeOwner == null || resumeOwner.getEmail() == null) {
                  throw new IllegalArgumentException("이력서 작성자 정보를 찾을 수 없습니다.");
              }

              // 프로필 이미지 경로 처리
              String profileImgPath = resumeOwner.getProfileImg();
              if (profileImgPath == null || profileImgPath.trim().isEmpty()) {
                  profileImgPath = " ";
              } else {
                  if (!profileImgPath.startsWith("/images/")) {
                      profileImgPath = "/images/profileImg/" + profileImgPath;
                  }
              }
              
              System.out.println("resumeOwner: " + resumeOwner.getName() + ", Email: " + resumeOwner.getEmail());

              
              InterviewOfferDTO corpInfo = talentPoolMapper.getCorpInfoByCorpNo(corpNo);
              if (corpInfo == null || corpInfo.getCorpName() == null) {
                throw new IllegalArgumentException("기업 정보가 잘못되었습니다.");
            }
              String corpName = corpInfo.getCorpName();
              String resumeTitle = resumeOwner.getTitle();
              String mailContent = resumeOwner.getName() + " 님, " + corpName +" 기업에서 귀하의 "+resumeTitle+" 이력서를 열람했습니다.";

              MessageDTO mail = new MessageDTO();
              mail.setCorpNo(corpNo);
              mail.setEmail(resumeOwner.getEmail());
              mail.setMessageTitle("["+corpName+"]에서 내 이력서를 열람하였습니다.");
              mail.setMessageContent(mailContent);
              mail.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
              mail.setIsRead("N");
              mail.setReadedAt("0");
              mail.setIsOffered("N");

              //열람 메세지(알람) 보내기
              talentPoolMapper.insertMessage(mail);
          }
      } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException("이력서 열람 및 메일 발송 처리 중 오류가 발생했습니다.", e);
      }
  }
  
  
  //최근본인재탭
  @Override
  public List<Integer> getRecentlyViewedResumes(Long corpNo, int startRow, int endRow) {
      Map<String, Object> params = new HashMap<>();
      params.put("corpNo", corpNo);
      params.put("startRow", startRow);
      params.put("endRow", endRow);
      return talentPoolMapper.getRecentlyViewedResumes(params);
  }

  @Override
  public List<TalentPoolDTO> getResumeDetailsBySeqs(List<Integer> resumeSeqs) {
      Map<String, Object> params = new HashMap<>();
      params.put("resumeSeqList", resumeSeqs);
      return talentPoolMapper.selectResumeMemberInfo(params);
  }

  @Override
  public int getRecentlyViewedResumesCount(Long corpNo) {
      return talentPoolMapper.getRecentlyViewedResumesCount(corpNo);
  }
  
  
	
}//class
