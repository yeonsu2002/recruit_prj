package kr.co.sist.mail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class JoinMailController {

	private final MailSendService mailSendService;
	private final EmailRepository emailRepository;
	
	@PostMapping("/signup/send-verification-email")
	public ResponseEntity<String> sendEmail(@RequestBody MailHtmlSendDTO mailHtmlSendDTO, HttpServletRequest request) {
		
		try {
			
			mailSendService.sendHtmlEmail(mailHtmlSendDTO); //인증메일 보냇어 
			
			//인증메일 보낸 후 테이블에 기록 하자 
			EmailVerificationEntity evEntity = new EmailVerificationEntity();
			evEntity.setEmail(mailHtmlSendDTO.getEmailAddr());
			evEntity.setVerificationCode(mailHtmlSendDTO.getContent());
			evEntity.setIp(request.getRemoteAddr());
			/**
			 * 실무에서는 IP를 꺼낼 때 String ip = request.getHeader("X-Forwarded-For"); 이런식으로 한다는데, 왜일까? 
			 * 그리고 IP도 개인정보보안 사항이라서 암호화 해야한다는데 맞나? 
			 */
			
			// 기존 객체 확인
      EmailVerificationEntity existing = emailRepository.findByEmail(mailHtmlSendDTO.getEmailAddr());

			
			if(emailRepository.findByEmail(evEntity.getEmail()) == null) { //처음 시도면 insert
				emailRepository.save(evEntity);
			} else if (emailRepository.findByEmail(evEntity.getEmail()).getCount() > 5) { //이미 인증시도를 5회 초과했다? 넌 아웃 
				return null;
			} else {
				int count = evEntity.getCount(); //아니면 시도횟수 +1
				evEntity.setCount(count+1);
			}
			
			
			
			return ResponseEntity.ok("메일이 전송되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("메일 전송 실패 : " + e.getMessage());
		}
		
		
	}
	
	
}
