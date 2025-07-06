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
@Table(name = "EDUCATION_HISTORY")
public class EducationHistoryEntity {

	@Id
	@Column(name = "EDUCATION_HISTORY_SEQ")
	private int educationHistorySeq;
	
	@Column(name = "RESUME_SEQ")
	private int resumeSeq;
	
	@Column(name = "INDEX_NUM")
	private int indexNum;
	
	@Column(name = "SCHOOL_NAME")
	private String schoolName;
	
	@Column(name = "DEPARTMENT")
	private  String department;
	
	@Column(name = "GRADE")
	private double grade;
	
	@Column(name = "STANDARD_GRADE")
	private double standardGrade;
	
	@Column(name = "EDUCATION_TYPE")
	private String educationType;
	
	@Column(name = "ADMISSION_DATE")
	private String admissionDate;
	
	@Column(name = "GRADUATE_DATE")
	private String graduateDate;
}
