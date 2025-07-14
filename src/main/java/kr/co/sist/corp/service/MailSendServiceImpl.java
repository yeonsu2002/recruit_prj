package kr.co.sist.corp.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.sist.corp.dto.MailHtmlSendDTO;

@Service
public class MailSendServiceImpl implements MailSendService {
	
  private final JavaMailSender mailSender;  //MailSender를 확장한 인터페이스로, MimeMessage 및 첨부파일 지원 등 추가 기능을 제공
  private final TemplateEngine templateEngine;

  public MailSendServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
  	this.mailSender = mailSender;
  	this.templateEngine = templateEngine;
  }
  
  
  /**
   * html기반 메일 전송 
   */
	@Override
	public void sendHtmlEmail(MailHtmlSendDTO mailHtmlSendDTO) {
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			Context context = new Context();
      context.setVariable("subject", mailHtmlSendDTO.getSubject());
      context.setVariable("message", mailHtmlSendDTO.getContent());
      if(mailHtmlSendDTO.getTarget().equals("user")) {
      	context.setVariable("userType", "일반 구직자");
      }
      if(mailHtmlSendDTO.getTarget().equals("corp")) {
      	context.setVariable("userType", "기업 회원");
      }
      
      // MailSendServiceImpl.java 내부
      String base64Image =  getBase64EncodedImage("static/images/logo.png");
      context.setVariable("logoImage", base64Image);
      
      String htmlContent = templateEngine.process("login/email-template", context);
      helper.setTo(mailHtmlSendDTO.getEmailAddr());
      helper.setSubject(mailHtmlSendDTO.getSubject());
      helper.setText(htmlContent, true);
      mailSender.send(message);
      System.out.println("Thymeleaf 템플릿 이메일 전송 성공!");
		} catch(MessagingException e) {
      System.out.println("[-] Thymeleaf 템플릿 이메일 전송 중 오류 발생: " + e.getMessage());
      throw new RuntimeException(e);

		} catch (IOException e) {
			e.printStackTrace();
      throw new RuntimeException(e);

		}
		
	}
	
		
	//이미지를 Base64로 인코딩하는 메서드
  private String getBase64EncodedImage(String imagePath) throws IOException {
      Resource resource = new ClassPathResource(imagePath); //classpath 내 리소스
      byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
      return Base64.getEncoder().encodeToString(bytes);
  }

}
