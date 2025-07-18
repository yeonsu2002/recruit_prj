package kr.co.sist.user.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobPostDTO {
	
    private Integer jobPostingSeq;
    private long corpNo;
    private Integer positionSeq;
    private String postingTitle;
    private String postingDetail;
    private String expLevel;
    private String postingEndDt;
    private String postingStartDt;
    private Integer recruitCnt;
    private String employType;
    private String workday;
    private String workStartTime;
    private String workEndTime;
    private Integer salary;
    private String contStartDt;
    private String contEndDt;
    private String eduLevel;
    private Integer viewCnt;
    private String zipcode;
    private String roadAddress;
    private String detailAddress;
    private String positionName;  
    private String stackName;
    private List<String> techStacks;
    
    private String corpNm;           // corp_nm
    private String corpInfo;         // corp_info
    private String corpUrl;       
    
    
    public void addTechStack(String stackName) {
      if (this.techStacks == null) {
          this.techStacks = new ArrayList<>();
      }
      if (stackName != null && !stackName.equals("기술 스택 없음")) {
          this.techStacks.add(stackName);
      }
  }
}
