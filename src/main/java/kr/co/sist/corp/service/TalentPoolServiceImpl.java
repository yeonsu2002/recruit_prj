package kr.co.sist.corp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.corp.dto.InterviewOfferDTO;
import kr.co.sist.corp.dto.MailDTO;
import kr.co.sist.corp.dto.ResumeScrapDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.mapper.TalentPoolMapper;
import kr.co.sist.user.mapper.ResumeMapper;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TalentPoolServiceImpl implements TalentPoolService {

	private final TalentPoolMapper talentPoolMapper;
	private final CipherUtil cu;
	
	@Override
	public List<TalentPoolDTO> getAllTalents(Long corpNo) {
	    List<TalentPoolDTO> list = talentPoolMapper.selectAllTalents();

	    for (TalentPoolDTO tDTO : list) {
	        try {
	            tDTO.setName(cu.decryptText(tDTO.getName()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

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
	
	//스크랩한 인재 리스트
	@Override
	public List<TalentPoolDTO> getScrappedTalents(Long corpNo) {
	    List<TalentPoolDTO> list = talentPoolMapper.selectScrappedTalents(corpNo);

	    for (TalentPoolDTO tDTO : list) {
	        try {
	            tDTO.setName(cu.decryptText(tDTO.getName()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        if (tDTO.getFinalEducation() == null || tDTO.getFinalEducation().trim().isEmpty()) {
	            tDTO.setFinalEducation("고등학교 졸업");
	        }

	        tDTO.setTotalCareer(formatCareer(tDTO.getTotalCareer()));
	        tDTO.setIsScrapped("Y");
	    }

	    return list;
	}
//	        boolean isScrapped = talentPoolMapper.isResumeScrapped(tDTO.getResumeSeq(), corpNo) > 0;
//	        tDTO.setIsScrapped(isScrapped ? "Y" : "N");
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

	
  @Override
  public List<TalentPoolDTO> getScrappedTalents(Long corpNo, int offset, int size) {
      return talentPoolMapper.selectPaginatedScrappedTalents(corpNo, offset, size);
  }

  @Override
  public int getScrappedTalentsCount(Long corpNo) {
      return talentPoolMapper.countScrappedTalents(corpNo);
  }

	//면접 제안
  @Override
  public void sendInterviewProposal(InterviewOfferDTO proposalDto) {
      talentPoolMapper.insertInterviewProposal(proposalDto);  // Mapper 호출

  }
  
  //이력서 상세
  @Override
  public TalentPoolDTO selectResumeDetail(int resumeNo) {
      TalentPoolDTO dto = talentPoolMapper.selectResumeDetail(resumeNo);

      // 복호화
      if (dto != null && dto.getName() != null) {
          dto.setName(cu.decryptText(dto.getName()));
      }
      return dto;
  }


  //이력서 열람
  @Override
  @Transactional
  public void viewResume(Long resumeSeq, Long corpNo) {
      // 1. 이력서 열람 여부 확인
      int count = talentPoolMapper.checkResumeViewExist(resumeSeq, corpNo);

      // 2. 열람 이력이 없을 경우만 로그 삽입 및 메일 발송
      if (count == 0) {
          // 2-1. 열람 로그 저장
          talentPoolMapper.insertResumeViewLog(resumeSeq, corpNo);

          // 2-2. 이력서 작성자 정보 조회
          TalentPoolDTO resumeOwner = talentPoolMapper.selectResumeInfo(resumeSeq);
          if (resumeOwner == null || resumeOwner.getEmail() == null) {
              throw new IllegalArgumentException("이력서 작성자 정보를 찾을 수 없습니다.");
          }

          // 프로필 이미지 경로 처리
          String profileImgPath = resumeOwner.getProfileImg();
          if (profileImgPath == null || profileImgPath.trim().isEmpty()) {
              profileImgPath = "/images/default_img.png";
          } else {
              // 예를 들어 DB에는 파일명만 저장되어 있으면 앞에 경로 붙이기
              if (!profileImgPath.startsWith("/images/")) {
                  profileImgPath = "/images/profileImg/" + profileImgPath;
              }
          }

          // 2-3. 메일 알림 생성
          String mailContent = "<p>" + resumeOwner.getName() + "님, 기업이 귀하의 이력서를 열람했습니다.</p>" +
                               "<img src='" + profileImgPath + "' alt='프로필 이미지' style='width:100px; height:100px;'/>";

          MailDTO mail = new MailDTO();
          mail.setCorpNo(corpNo);
          mail.setEmail(resumeOwner.getEmail());
          mail.setMailTitle("이력서 열람 알림");
          mail.setMailContent(mailContent);
          mail.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
          mail.setIsRead("0");
          mail.setReadedAt(null);
          mail.setIsOffered(1);

          // 2-4. 메일 DB 저장
          talentPoolMapper.insertMail(mail);
      }
  }
	
}//class
