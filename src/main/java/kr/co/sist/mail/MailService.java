package kr.co.sist.mail;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class MailService {

	public void sendEmailVerificationCode() {
		
		int secureNumber = createSecureNumber();
		
		
		
	}
	
	
	
	//인증번호, 토큰, 비밀번호 생성 등 보안에 민감한 기능에는 SecureRandom이 강력 추천됩니다.
	//보안상 더 강력하다는데, 뭐가? : https://chatgpt.com/s/t_6863bf3e289c8191b1239d8a4f0ee1b2
	public int createSecureNumber() {
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
