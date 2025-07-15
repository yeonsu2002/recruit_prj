package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.MessageEntity;
import kr.co.sist.user.mapper.JobPostingMapper;
import kr.co.sist.user.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
	
	private final MessageRepository mailRepos;
	private final JobPostingMapper jobPostingMapper;

	public void addResumeReadNotification(UserDTO userDTO,  CorpEntity corpEntity, int jobPostingSeq) {

		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		String postingTitle = jobPostingMapper.selectJobPostingById(jobPostingSeq).getPostingTitle();
		
		String title = "[" + corpEntity.getCorpNm() + "]에서 지원하신 이력서를 확인했습니다.";
		String content = "안녕하세요, [" + userDTO.getName() + "]님.\n\n" +
        corpEntity.getCorpNm() + "에서 지원하신 \"" + postingTitle + "\" 이력서를 확인하였습니다.\n" +
        "검토 후 결과를 안내해 드릴 예정이니 조금만 기다려 주세요.\n\n감사합니다.";
				
		MessageEntity message = new MessageEntity();
		message.setCorpNo(corpEntity.getCorpNo());
		message.setEmail(userDTO.getEmail());
		message.setMessageTitle(title);
		message.setMessageContent(content);
		message.setCreatedAt(now.toString());
		
		mailRepos.save(message);
	}// addResumeReadNotification
}
