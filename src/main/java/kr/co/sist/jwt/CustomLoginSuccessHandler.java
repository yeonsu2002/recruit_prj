package kr.co.sist.jwt;

import java.io.IOException;
import java.time.Duration;

import org.springframework.http.HttpStatus;
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
		
		// JWT 발급 -> access, refresh 토큰발급(07.23 변경)  
    String access = jwtUtil.createJwt("access", customUser.getUserDTO(), 60 * 10 * 1000L); //10분
    String refresh = jwtUtil.createJwt("refresh", customUser.getUserDTO(), 60 * 60 * 86400000L); //24시간 
    
    //System.out.println("CustomLoginSuccessHandler / 디버깅 access : " + access);
    //System.out.println("CustomLoginSuccessHandler / 디버깅 refresh : " + refresh);
    
    // access -> header에(그 후 로컬스토리지로..) 저장.
    response.setHeader("access", access);
    
    asdfasdvasfvsdfvas //여기서 안되고 있다. 표시 
    
    
    // refresh -> 쿠키 생성 후 저장
    ResponseCookie cookie = ResponseCookie.from("refresh", refresh) //ResponseCookie : 쿠키를 String 형태로 직접 생성해 주는 빌더
	    .httpOnly(true)//js접근불가 (document.cookie 불가하게됨) -> xss공격을 방어 
	    //.secure(false) //HTTPS에서만 동작 (개발시 false)
	    .sameSite("Strict") //CSRF방지 
	    .path("/") //전체경로에 대해 쿠키 전송 
	    //.maxAge(Duration.ofHours(1)) //쿠키 1시간 유지 (JWT보다 길어야 겠지 )
	    .maxAge(Duration.ofHours(24)) //refresh 쿠키는 24시간 유지 
	    .build();

    response.setHeader("Set-Cookie", cookie.toString()); // "Set-Cookie"는 서버 → 클라이언트로 쿠키를 보내기 위한 공식 HTTP 응답 헤더
    
    response.setStatus(HttpStatus.OK.value());
    
    //기업회원 로그인 성공시, 기업메인페이지로 이동  
    boolean hasRoleCorp = customUser.getAuthorities().stream()
    												.anyMatch(auth -> auth.getAuthority().equals("ROLE_CORP"));
    
    //일반회원 로그인 성공시, 유저메인페이지로 이동 
    boolean hasRoleUser = customUser.getAuthorities().stream()
    												.anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    
    //원하는 대로 리다이렉트 
    if(hasRoleCorp) {
    	response.sendRedirect("/corp/main");
    } 
    if(hasRoleUser) {
    	response.sendRedirect("/");
    }
		
	}

}
