package kr.co.sist.corp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.ResumeScrapDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.mapper.TalentPoolMapper;
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

	@Override
	public int selectTalentTotalCount() {
		return talentPoolMapper.selectTalentTotalCount();
	}//selectTalentTotalCount
	
	
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

	



	
}//class
