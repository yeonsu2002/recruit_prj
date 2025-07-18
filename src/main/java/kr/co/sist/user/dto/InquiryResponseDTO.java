package kr.co.sist.user.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryResponseDTO {
    private Long askSeq;
    private String email;
    private String adminId;
    private String title;
    private String content;
    private String answer;
    private LocalDateTime regsDate;
    private String category;
    private String answerStat;
    private String attachFile;
    private String userType;
    public InquiryResponseDTO() {
    	
    }
}