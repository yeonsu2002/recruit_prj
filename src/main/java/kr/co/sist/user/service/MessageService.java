package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.entity.MessageEntity;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.mapper.JobPostingMapper;
import kr.co.sist.user.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepos;
	private final JobPostingMapper jobPostingMapper;

	// 지원서 읽을 시 메시지 보내기
	public void addResumeReadNotification(UserEntity userEntity, CorpEntity corpEntity, int jobPostingSeq) {

		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		JobPostDTO jobPostDTO = jobPostingMapper.selectJobPostingsBySeq(jobPostingSeq);

		String title = "[" + corpEntity.getCorpNm() + "]에서 지원하신 이력서를 확인했습니다.";
		String content = "안녕하세요, [" + userEntity.getName() + "]님.\n\n" + corpEntity.getCorpNm() + "에서 지원하신 \""
				+ jobPostDTO.getPostingTitle() + "\" 이력서를 확인하였습니다.\n" + "검토 후 결과를 안내해 드릴 예정이니 조금만 기다려 주세요.\n\n감사합니다.";

		// 메시지 객체 생성
		MessageEntity message = new MessageEntity();
		message.setCorpNo(corpEntity.getCorpNo());
		message.setEmail(userEntity.getEmail());
		message.setMessageTitle(title);
		message.setMessageContent(content);
		message.setCreatedAt(now.toString());

		messageRepos.save(message);
	}// addResumeReadNotification

	// 합격 상태 변경 시 메시지 보내기
	public void addMessage(String email, long corpNo, String title, String content) {

		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		
		MessageEntity message = new MessageEntity();
		message.setEmail(email);
		message.setCorpNo(corpNo);
		message.setMessageTitle(title);
		message.setMessageContent(content);
		message.setCreatedAt(now.toString());
		
		messageRepos.save(message);

	}
}
