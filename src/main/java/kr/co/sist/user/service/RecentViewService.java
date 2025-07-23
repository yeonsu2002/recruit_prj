package kr.co.sist.user.service;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.RecentViewPostingDTO;
import kr.co.sist.user.mapper.RecentViewPostingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecentViewService {
	
	private final RecentViewPostingMapper recentViewMapper;

	public void saveRecentViewPosting(String email, Integer jobPostingSeq) {
	
		try {
			
			RecentViewPostingDTO existingView=recentViewMapper.selectRecentView(email,jobPostingSeq);
			
			if(existingView !=null) {
				
				recentViewMapper.updateRecentViewTime(existingView.getRecentViewPostingSeq());
				
			}else {
				
				RecentViewPostingDTO newView=new RecentViewPostingDTO();
				newView.setEmail(email);
				newView.setJobPostingSeq(jobPostingSeq);
				recentViewMapper.insertRecentView(newView);
			}
			
			recentViewMapper.deleteOldRecentViews(email, 10);
			
		}catch(Exception e){
			
			log.error("최신본 공고 저장 실패 : email={}, jobPostingSeq={}", email, jobPostingSeq, e);
			
		}
		
	}
	
}
