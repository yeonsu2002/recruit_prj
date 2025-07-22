package kr.co.sist.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.RecentViewPostingDTO;

@Mapper
public interface RecentViewPostingMapper {
	
	int insertRecentViewed(RecentViewPostingDTO dto);

}
