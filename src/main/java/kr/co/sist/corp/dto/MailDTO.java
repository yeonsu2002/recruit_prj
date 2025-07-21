package kr.co.sist.corp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailDTO {
    private Long corpNo;
    private String email;
    private String mailTitle;
    private String mailContent;
    private String createdAt;
    private String isRead;
    private String readedAt;
    private Integer isOffered; 
}
