package kr.co.sist.admin.ask;

import java.time.format.DateTimeFormatter;

import kr.co.sist.user.entity.InquiryEntity;
import lombok.Getter;

@Getter
public class AdminInquiryDTO {

    private Long askSeq;
    private String email;
    private String title;
    private String category;
    private String regsDate;
    private String answerStat;

    public AdminInquiryDTO(InquiryEntity entity) {
        this.askSeq = entity.getAskSeq();
        this.email = entity.getEmail();
        this.title = entity.getTitle();
        this.category = entity.getCategory();
        this.answerStat = entity.getAnswerStat();
        this.regsDate = entity.getRegsDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
