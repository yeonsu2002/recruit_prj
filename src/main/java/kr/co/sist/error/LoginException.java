package kr.co.sist.error;

import org.springframework.stereotype.Component;

public class LoginException extends RuntimeException {
	
	public LoginException(String message) {
		super(message);
	}

}
