package kr.co.sist.corp.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("corpAllicantSearchDTO")
public class ApplicantSearchDTO {

	private long corpNo;
	private int size;
  private String postingStatus;
  private int postingTitle;
  private int applicationStatus;
  private int passStage;
  private String keyword;
}
