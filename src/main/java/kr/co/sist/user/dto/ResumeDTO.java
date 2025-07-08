package kr.co.sist.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResumeDTO {

	private int resumeSeq;
	private String email, title, image, introduction, createdAt, updatedAt, careerType, isPublic;
}
