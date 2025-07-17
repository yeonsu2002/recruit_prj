package kr.co.sist.user.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.user.service.MessageService;
import kr.co.sist.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestMyPageController {

	private final MyPageService myPageServ;
	private final MessageService messageServ;

	// 지원한 이력서 지원 취소
	@PutMapping("/mypage/application/{jobApplicationSeq}")
	public String deleteApplication(@PathVariable int jobApplicationSeq) {

		int result = myPageServ.cancelApplication(jobApplicationSeq);

		if (result >= 1) {
			return "success";
		}

		return "fail";

	}// deleteApplication

	// 메일 읽음
	@PutMapping("/mypage/message/{messageSeq}")
	public void readMessage(@PathVariable int messageSeq) {

		// 메일 읽음 처리
		messageServ.readMessage(messageSeq);

	}
}
