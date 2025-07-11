package kr.co.sist.error;

import org.springframework.stereotype.Component;

public class NotFoundException extends RuntimeException {
	
	public NotFoundException(String message) {
		super(message);
	}

}
