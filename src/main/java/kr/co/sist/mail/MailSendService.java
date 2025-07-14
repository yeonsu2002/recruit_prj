package kr.co.sist.mail;

import org.springframework.stereotype.Service;

@Service
public interface MailSendService {

	public void sendHtmlEmail(MailHtmlSendDTO mailHtmlSendDTO); // MimeMessageHelper를 활용하여 HTML 기반 메일을 전송합니다.
}
