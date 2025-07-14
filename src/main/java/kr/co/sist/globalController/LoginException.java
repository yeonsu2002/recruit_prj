package kr.co.sist.globalController;

import org.springframework.stereotype.Component;

public class LoginException extends RuntimeException {
	
	public LoginException(String message) {
		super(message);
	}

}
