package kr.co.sist.user.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("userAttachmentDTO")
public class AttachmentDTO {

	private int attachmentSeq;
	private String email, fileName, fileType, createdAt;
	private long fileSize;
}
