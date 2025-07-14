package kr.co.sist.corp.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("corpApplicationAttachmentDTO")
public class ApplicationAttachmentDTO {

	private String fileName;
	private int applicationAttachmentSeq, attachmentSeq, jobApplicationSeq;
}
