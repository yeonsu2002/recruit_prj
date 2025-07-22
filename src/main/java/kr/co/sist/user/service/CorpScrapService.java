package kr.co.sist.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.CorpScrapDTO;
import kr.co.sist.user.mapper.CorpScrapMapper;

@Service
public class CorpScrapService {
    
    @Autowired
    private CorpScrapMapper corpScrapMapper;

    // 이미 스크랩했는지 체크
    public boolean isAlreadyScrapped(long corpNo, String email) {
        return corpScrapMapper.countScrap(corpNo, email) > 0;
    }
    
    // 기업 스크랩 추가
    public void addCorpScrap(CorpScrapDTO corpScrapDTO) {
        corpScrapMapper.insertScrap(corpScrapDTO);
    }
    
    // 기업 스크랩 삭제
    public boolean removeCorpScrap(long corpNo, String email) {
        return corpScrapMapper.deleteScrap(corpNo, email) > 0;
    }
}

