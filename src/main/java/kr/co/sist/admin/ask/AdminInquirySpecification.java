package kr.co.sist.admin.ask;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import kr.co.sist.user.entity.InquiryEntity;

public class AdminInquirySpecification {

	public static Specification<InquiryEntity> filterBy(String category, String answerStat, String keyword, String keywordType) {
    return (root, query, cb) -> {
        Predicate predicate = cb.conjunction();

        // 카테고리가 "전체"일 경우 조건 생략
        if (category != null && !category.equals("전체") && !category.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("category"), category));
        }

        // 답변 상태가 "전체"일 경우 조건 생략
        if (answerStat != null && !answerStat.equals("전체") && !answerStat.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("answerStat"), answerStat));
        }

        // 검색어가 있으면 키워드 타입에 맞춰 검색
        if (keyword != null && !keyword.isEmpty() && keywordType != null) {
            switch (keywordType) {
                case "title":
                    predicate = cb.and(predicate, cb.like(root.get("title"), "%" + keyword + "%"));
                    break;
                case "email":
                    predicate = cb.and(predicate, cb.like(root.get("email"), "%" + keyword + "%"));
                    break;
                case "content":
                    predicate = cb.and(predicate, cb.like(root.get("content"), "%" + keyword + "%"));
                    break;
            }
        }

        return predicate;
    };
    }
}
