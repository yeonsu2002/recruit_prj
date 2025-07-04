package kr.co.sist.corp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorpDTO {

	private Integer corpNo;
	private String corpNm;
	private String corpInfo;
	private String corpUrl;
	private String corpLogo;
	private String corpImg;
	private Integer corpAvgSal;
	private Integer corpAnnualRevenue;
	private String corpCreatedAt;
	private Integer corpEmpCnt;
	private String bizCert;
	private String corpAiActive;
	private String industry;
	private String companySize;
	
	private List<TagDTO> tagList; 
	
}
