package kr.co.sist.mail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

//lombok이 생성자주입 간소화
@RequiredArgsConstructor
@Controller
@RequestMapping("/mail")
public class MailController {
	
	private final MailService mailService;
	
	@PostMapping("/sendVfCode")
	@ResponseBody
	public void sendVerifyCodeMail(@RequestParam String to) {
		
	}
	
	
}
