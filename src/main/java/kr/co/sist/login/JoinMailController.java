package kr.co.sist.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.corp.dto.MailHtmlSendDTO;
import kr.co.sist.corp.service.MailSendService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class JoinMailController {

	private final MailSendService mailSendService;
	
	@PostMapping("/signup/send-verification-email")
	public String sendEmail(@RequestBody MailHtmlSendDTO mailHtmlSendDTO) {
		mailSendService.sendHtmlEmail(mailHtmlSendDTO);
		
		return "메일이 전송되었습니다.";
	}
	
	
}
