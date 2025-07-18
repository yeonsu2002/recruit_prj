package kr.co.sist.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.sist.user.entity.InquiryEntity;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>, JpaSpecificationExecutor<InquiryEntity> {
    
    // 사용자 이메일로 문의 목록 조회 (최신순)
    Page<InquiryEntity> findByEmailOrderByRegsDateDesc(String email, PageRequest pageRequest);
    
    // 카테고리별 문의 통계
    @Query("SELECT i.category, COUNT(i) FROM InquiryEntity i GROUP BY i.category")
    List<Object[]> getInquiryStatsByCategory();
    
    // 답변 상태별 문의 통계
    @Query("SELECT i.answerStat, COUNT(i) FROM InquiryEntity i GROUP BY i.answerStat")
    List<Object[]> getInquiryStatsByAnswerStatus();
}
