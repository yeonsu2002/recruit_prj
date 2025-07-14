package kr.co.sist.corp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.repository.CorpEditRepository;

import java.util.Optional;

@Service
@Transactional
public class CorpEditService {

    @Autowired
    private CorpEditRepository corpRepository;

    // 기업정보 조회
    public CorpEntity getCorpInfo(Long corpNo) {
        Optional<CorpEntity> corpEntity = corpRepository.findById(corpNo);
        return corpEntity.orElse(null);
    }

    // 기업정보 수정
    public void updateCorpInfo(CorpEntity corpEntity) {
        // 기존 정보 조회
        Optional<CorpEntity> existingCorp = corpRepository.findById(corpEntity.getCorpNo());
        
        if (existingCorp.isPresent()) {
            CorpEntity existing = existingCorp.get();
            
            // 수정 가능한 필드만 업데이트 (읽기 전용 필드는 제외)
            if (corpEntity.getCorpInfo() != null) {
                existing.setCorpInfo(corpEntity.getCorpInfo());
            }
            if (corpEntity.getCorpUrl() != null) {
                existing.setCorpUrl(corpEntity.getCorpUrl());
            }
            if (corpEntity.getCorpAvgSal() != null) {
                existing.setCorpAvgSal(corpEntity.getCorpAvgSal());
            }
            if (corpEntity.getCorpAnnualRevenue() != null) {
                existing.setCorpAnnualRevenue(corpEntity.getCorpAnnualRevenue());
            }
            if (corpEntity.getCorpCreatedAt() != null) {
                existing.setCorpCreatedAt(corpEntity.getCorpCreatedAt());
            }
            if (corpEntity.getCorpEmpCnt() != null) {
                existing.setCorpEmpCnt(corpEntity.getCorpEmpCnt());
            }
            if (corpEntity.getCorpAiActive() != null) {
                existing.setCorpAiActive(corpEntity.getCorpAiActive());
            }
            if (corpEntity.getIndustry() != null) {
                existing.setIndustry(corpEntity.getIndustry());
            }
            if (corpEntity.getCompanySize() != null) {
                existing.setCompanySize(corpEntity.getCompanySize());
            }
            
            // 읽기 전용 필드들은 업데이트하지 않음
            // corpNm, bizCert, corpCeo는 수정 불가
            
            corpRepository.save(existing);
        } else {
            throw new RuntimeException("기업 정보를 찾을 수 없습니다.");
        }
    }

    // 기업정보 존재 여부 확인
    public boolean existsCorpInfo(Long corpNo) {
        return corpRepository.existsById(corpNo);
    }

    // 기업명으로 검색
    public CorpEntity findByCorpName(String corpName) {
        return corpRepository.findByCorpNm(corpName);
    }

    // 사업자등록번호로 검색
    public CorpEntity findByBizCert(String bizCert) {
        return corpRepository.findByBizCert(bizCert);
    }
}