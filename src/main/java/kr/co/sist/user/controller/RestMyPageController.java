package kr.co.sist.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestMyPageController {

	private final MyPageService myPageServ;

	// 지원한 이력서 지원 취소
	@DeleteMapping("/mypage/application/{jobApplicationSeq}")
	public String deleteApplication(@PathVariable int jobApplicationSeq) {

		int result = myPageServ.cancelApplication(jobApplicationSeq);

		if (result >= 1) {
			return "success";
		}

		return "fail";

	}// deleteApplication
}
