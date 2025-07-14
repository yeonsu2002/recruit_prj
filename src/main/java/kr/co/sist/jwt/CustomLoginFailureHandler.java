package kr.co.sist.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String errorMsg = "알 수 없는 로그인 오류가 발생하였습니다.";
		
		if(exception instanceof UsernameNotFoundException) { //스프링 시큐리티 보안정책으로 인해 UsernameNotFoundException는 BadCredentialsException로 덮어씌워짐
			errorMsg = "등록되지 않은 이메일입니다.";
		} else if (exception instanceof BadCredentialsException) {
			errorMsg = "이메일 또는 비밀번호가 일치하지 않습니다.";
		} else if (exception instanceof LockedException) {
			errorMsg = "계정이 잠겨 있습니다.";
		} else if (exception instanceof DisabledException) {
			errorMsg = "계정이 비활성화되어있습니다.";
		} else if (exception instanceof AccountExpiredException) {
			errorMsg = "계정이 만료되었습니다.";
		} else if (exception instanceof CredentialsExpiredException) {
			errorMsg = "비밀번호 유효기간이 만료되었습니다.";
		}
		
		// 메시지를 쿼리파라미터로 전달 (GET 방식으로 리다이렉트)
		System.out.println("디버깅 : " + errorMsg);
		response.sendRedirect("/login?error=true&errorMessage=" + java.net.URLEncoder.encode(errorMsg, "UTF-8"));
		 
	}

}
