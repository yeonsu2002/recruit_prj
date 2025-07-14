package kr.co.sist.corp.service;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.MailHtmlSendDTO;

@Service
public interface MailSendService {

	public void sendHtmlEmail(MailHtmlSendDTO mailHtmlSendDTO); // MimeMessageHelper를 활용하여 HTML 기반 메일을 전송합니다.
}
