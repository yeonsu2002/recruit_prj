package kr.co.sist.corp.dto;

import lombok.Data;

@Data
public class TalentPoolDTO {

    private Long resumeSeq;
    private String name;
    private String gender;
    private String birthYear;
    private String shortAddress;
    
    private String totalCareer;
    private String finalEducation;
    private String desiredPositions;
    private String techStacks;
    private String certifications;
    
    private Long corpNo;
    private String isScrapped;
    
}