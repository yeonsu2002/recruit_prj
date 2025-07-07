package kr.co.sist.corp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorpDTO {

	private Long corpNo;
	private String corpNm;
	private String corpInfo;
	private String corpUrl;
	private String corpLogo;
	private String corpImg;
	private Long corpAvgSal;
	private Long corpAnnualRevenue;
	private String corpCreatedAt;
	private Integer corpEmpCnt;
	private String bizCert;
	private String corpAiActive;
	private String industry;
	private String companySize;
	
	private String uploadedFileName; // 저장된 파일 이름
	
	private List<TagDTO> tagList; 
	
}
