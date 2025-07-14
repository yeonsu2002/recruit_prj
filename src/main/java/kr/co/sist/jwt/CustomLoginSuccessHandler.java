package kr.co.sist.jwt;

import java.io.IOException;
import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * SecurityConfig의 로그인
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JWTUtil jwtUtil;
	
	public CustomLoginSuccessHandler(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		// JWT 발급
    String token = jwtUtil.createJwt( customUser.getUserDTO(), 60 * 60 * 1000L);
    
    // 쿠키로 내려줌
    ResponseCookie cookie = ResponseCookie.from("Authorization", token)
	    .httpOnly(true)//js접근불가 
	    .secure(false) //HTTPS에서만 동작 (개발시 false)
	    .sameSite("Strict") //CSRF방지 
	    .path("/") //전체경로에 대해 쿠키 전송 
	    .maxAge(Duration.ofHours(1)) //쿠키 1시간 유지 (JWT보다 길어야 겠지 )
	    .build();

    response.setHeader("Set-Cookie", cookie.toString());
    
    //기업회원 로그인 성공시, 기업메인페이지로 이동  
    boolean hasRoleCorp = customUser.getAuthorities().stream()
    												.anyMatch(auth -> auth.getAuthority().equals("ROLE_CORP"));
    
    boolean hasRoleUser = customUser.getAuthorities().stream()
    												.anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    
    System.out.println(hasRoleCorp);
    
    //원하는 대로 리다이렉트 
    if(hasRoleCorp) {
    	response.sendRedirect("/corp/main");
    } 
    if(hasRoleUser) {
    	response.sendRedirect("/");
    }
		
	}

}
