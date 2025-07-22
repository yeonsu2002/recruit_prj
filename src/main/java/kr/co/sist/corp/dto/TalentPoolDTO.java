package kr.co.sist.corp.dto;

import lombok.Data;

@Data
public class TalentPoolDTO {

    private Long resumeSeq;
    private Long corpNo;
    private String isScrapped;
    
    private String name;
    private String email;
    private String gender;
    private String birthYear;
    private String shortAddress;
    private String totalCareer;
    private String finalEducation;
    private String desiredPositions;
    private String techStacks;
    private String certifications;
    private String latestDate;
		public String ProfileImg;
    
    
    
}