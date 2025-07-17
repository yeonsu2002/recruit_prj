package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.dto.MessageDTO;

@Mapper
public interface MessageMapper {
	
	public JobPostDTO selectPostTitle(int jobPostingSeq);
	
	public List<MessageDTO> selectMyMessage(String email);
}
