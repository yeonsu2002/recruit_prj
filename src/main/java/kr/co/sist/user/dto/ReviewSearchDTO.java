package kr.co.sist.user.dto;

import lombok.Data;

@Data
public class ReviewSearchDTO {
	
	private Long corpNo;
	private int page =1;
	private int size=10;
	
	public int getOffset(){
		return (page -1) * size;
	}

}
