package kr.co.sist.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
	
	    private Integer noticeSeq;
	    private String adminId;
	    private String title;
	    private String content;
	    private String regsDate;
	    private String modifyDate;
	

}
