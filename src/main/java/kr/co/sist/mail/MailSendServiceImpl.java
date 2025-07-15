package kr.co.sist.mail;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
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
			mailHtmlSendDTO.setContent(String.valueOf(createSecureNumber()));//난수를 Context에 담아줘. 
			
      context.setVariable("subject", mailHtmlSendDTO.getSubject());
      context.setVariable("message", mailHtmlSendDTO.getContent());
      if(mailHtmlSendDTO.getTarget().equals("user")) {
      	context.setVariable("userType", "일반 구직자");
      }
      if(mailHtmlSendDTO.getTarget().equals("corp")) {
      	context.setVariable("userType", "기업 회원");
      }
      
      // MailSendServiceImpl.java 내부
      //String base64Image =  getBase64EncodedImage("static/images/logo.png");
      //context.setVariable("logoImage", base64Image);
      
      // static/images/logo.png 파일을 CID 이름으로 첨부
      File logoFile = new ClassPathResource("static/images/logo.png").getFile();
      helper.addInline("logoImage", logoFile);
      context.setVariable("logoImage", logoFile);
      
      
      String htmlContent = templateEngine.process("login/email-template", context);
      helper.setTo(mailHtmlSendDTO.getEmailAddr());
      helper.setSubject(mailHtmlSendDTO.getSubject());
      helper.setText(htmlContent, true);
      mailSender.send(message);
      
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
  
  //인증번호, 토큰, 비밀번호 생성 등 보안에 민감한 기능에는 SecureRandom이 강력 추천됩니다.
	//보안상 더 강력하다는데, 뭐가? : https://chatgpt.com/s/t_6863bf3e289c8191b1239d8a4f0ee1b2
	private int createSecureNumber() {
		int secureNumber = -1;
		
		SecureRandom secureRandom = new SecureRandom();

		// 6자리 인증번호
		secureNumber = secureRandom.nextInt(900000) + 100000; // 100000 ~ 999999
	
		// 랜덤 바이트 배열 (예: 토큰 생성)
		byte[] tokenBytes = new byte[16];
		secureRandom.nextBytes(tokenBytes);
		
		return secureNumber;
	}
	
	
	

}
