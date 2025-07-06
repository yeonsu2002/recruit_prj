package kr.co.sist.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Entity
@Table(name = "CAREER")
public class CareerEntity {

	@Id
	@Column(name = "CAREER_SEQ")
	private int careerSeq;
	
	@Column(name = "RESUME_SEQ")
	private int resumeSeq;
	
	@Column(name = "INDEX_NUM")
	private int indexNum;
	
	@Column(name = "COMPANY_NAME")
	private String companyName;
	
	@Column(name = "POSITION")
	private String position;
	
	@Column(name = "CAREER_DESCRIPTION")
	private String careerDescription;
	
	@Column(name = "START_DATE")
	private String startDate;
	
	@Column(name = "END_DATE")
	private String endDate;
	
}
