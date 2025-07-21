package kr.co.sist.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.MessageDTO;
import kr.co.sist.user.dto.MessageSearchDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.MessageService;
import kr.co.sist.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestMyPageController {

	private final MyPageService myPageServ;
	private final MessageService messageServ;

	private final UserRepository userRepos;

	// 지원한 이력서 지원 취소
	@PutMapping("/mypage/application/{jobApplicationSeq}")
	public String deleteApplication(@PathVariable int jobApplicationSeq) {

		int result = myPageServ.cancelApplication(jobApplicationSeq);

		if (result >= 1) {
			return "success";
		}

		return "fail";

	}// deleteApplication

	// 검색 & 페이징 된 메일 목록 가져오기
	@GetMapping("/mypage/messages")
	public Map<String, Object> returnMessage(@AuthenticationPrincipal CustomUser userInfo,
			@RequestBody MessageSearchDTO searchDTO) {

		Map<String, Object> result = new HashMap<>();

		UserEntity userEntity = userRepos.findById(userInfo.getEmail()).orElse(null);

		// 모든 메일 개수 가져오기
		int totalCnt = messageServ.cntMyAllMesage(userEntity.getEmail());

		// 검색조건에 유저 설
		searchDTO.setEmail(userEntity.getEmail());

		// 검색된 메일 개수 가져오기
		int searchCnt = messageServ.cntMyMessage(searchDTO);

		// 페이징 된 메일 목록 가져오기
		searchDTO.setTotalCnt(totalCnt);
		searchDTO.setSearchCnt(searchCnt);
		List<MessageDTO> messages = messageServ.searchMyMessage(searchDTO);

		result.put("result", "success");
		result.put("data", messages);

		return result;
	}

	// 메일 읽음
	@PutMapping("/mypage/message/{messageSeq}")
	public void readMessage(@PathVariable int messageSeq) {

		// 메일 읽음 처리
		messageServ.toggleReadMessage(messageSeq);

	}// readMessage

	// 체크된 메일들 읽음 여두 토글처리
	@PutMapping("/mypage/messages/{selectedSeq}")
	public void readMessages(@PathVariable List<Integer> selectedSeq) {

		for (int messageSeq : selectedSeq) {
			messageServ.toggleReadMessage(messageSeq);
		}
	}// readMessages
	
	//체크된 메일들 삭제
	@DeleteMapping("/mypage/messages/{selectedSeq}")
	public void removeMessages(@PathVariable List<Integer> selectedSeq) {
		
		for(int messageSeq : selectedSeq) {
			messageServ.removeMessage(messageSeq);
		}
	}//deleteMessages
	
}// class
