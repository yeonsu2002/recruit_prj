package kr.co.sist.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.sist.user.dto.ReviewDTO;
import kr.co.sist.user.dto.ReviewSearchDTO;

@Mapper
public interface ReviewMapper {
    
    /**
     * 해당 기업의 모든 리뷰 조회
     */
    List<ReviewDTO> selectReviewsByCorpNo(Long corpNo);
    
    /**
     * 페이징된 리뷰 목록 조회
     */
    List<ReviewDTO> selectReviewsPage(ReviewSearchDTO searchDTO);
    
    /**
     * 해당 기업의 리뷰 총 개수 조회
     */
    int countReviewsByCorpNo(Long corpNo);
    
    /**
     * 리뷰 통계 조회 (평균 평점, 총 리뷰 수)
     */
    ReviewDTO selectReviewStats(Long corpNo);
    
    /**
     * 기업명 조회
     */
    String selectCompanyName(Long corpNo);
    
    /**
     * 리뷰 작성 권한 확인
     */
    int checkReviewEligibility(String email, Long corpNo);
    
    /**
     * 이미 리뷰를 작성했는지 확인
     */
    int checkExistingReview(String email, Long corpNo);
    
    /**
     * 리뷰 저장
     */
    int insertReview(ReviewDTO dto);
}