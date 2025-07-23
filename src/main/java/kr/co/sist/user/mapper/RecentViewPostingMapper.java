package kr.co.sist.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.user.dto.RecentViewPostingDTO;

@Mapper
public interface RecentViewPostingMapper {
	
	RecentViewPostingDTO selectRecentView(@Param("email") String email,
																				@Param("jobPostingSeq") Integer jobPostingSeq);
	

	void insertRecentView(RecentViewPostingDTO dto);
	
	 // 기존 기록의 시간 업데이트
  void updateRecentViewTime(@Param("recentViewPostingSeq") Integer recentViewPostingSeq);
  
  // 오래된 기록 삭제 (사용자당 최대 개수 유지)
  void deleteOldRecentViews(@Param("email") String email, @Param("maxRecords") int maxRecords);
  
}
