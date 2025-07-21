package kr.co.sist.corp.dto;

import lombok.Data;

@Data
public class InterviewOfferDTO {

  private Long corpNo;
  private Long resumeSeq;
  private String email;
  private String mailTitle;
  private String mailContent;
}