package kr.co.sist.user.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobPostDTO {
	
    private int jobPostingSeq;
    private int corpNo;
    private int positionSeq;
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
    private int salary;
    private String contStartDt;
    private String contEndDt;
    private String eduLevel;
    private int viewCnt;
    private String zipcode;
    private String roadAddress;
    private String detailAddress;
    private String positionName;  
    private String stackName;
    private List<String> techStacks;
    
    
    private String corpNm;           // corp_nm
    private String corpInfo;         // corp_info
    private String corpUrl;       
    
}
