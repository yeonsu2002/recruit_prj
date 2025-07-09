package kr.co.sist.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.DTO.CustomOAuth2User;
import kr.bit.DTO.MemberDTO;

/*
 * 스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 커스텀 필터를 등록해야 한다.
 * 해당 필터를 통해 요청 쿠키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성한다. 
 * (이 세션은 STATLESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸 된다.)
 * */

//JWTFilter의 역할 = 쿠키 검증 -> 세션저장 
public class JWTFilter_Old extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	
	public JWTFilter_Old(JWTUtil jwtUtil) {
		
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("JWTFilter 통과");
		
    //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾기 
		String authorization = null;
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
		    System.out.println("No cookies found in the request");
		    filterChain.doFilter(request, response);
		    return;
		}
		
		for(Cookie cookie : cookies) {
			
			System.out.println(cookie.getName());
			if(cookie.getName().equals("Authorization")) {
				authorization = cookie.getValue();
			}
		} 
		
		String token = authorization; // token에는 Authorization 쿠키의 Value가 들어가게 됨(사용자 정보), 아래 메서드에 사용됨 

		//	1. 쿠키 Authorization의 값이 null인지 검증
		if(token == null) {
			
			System.out.println("Authorization Token is Null");
			filterChain.doFilter(request, response);
			
			//조건이 해당되면 메소드 종료(필수)
			return;
		}
		
		//	2. 일단 Authorization 값이 null은 아님 -> 토큰의 소멸시간 검증
		if(jwtUtil.isExpired(token)) {
			
			System.out.println("token is Expired: 토큰 만료");
			filterChain.doFilter(request, response);
            
			//조건이 해당되면 필터체인 종료 (필수)
			return;
		}
		
		//3. 쿠키는 정상임 -> 세션에 저장할거임 

		
		//쿠키에서 정보빼기 -> UserDTO생성 -> OAuth2User객체 생성 -> 시큐리티 인증토큰 생성 -> 세션에 사용자 등록 
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		String name = jwtUtil.getName(token);
		
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setUsername(username);
		memberDTO.setRole(role);
		memberDTO.setName(name);
		
		//UserDetails에 회원 정보 객체 담기
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO);
		
		//스프링 시큐리티 인증토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
		
		//세션에 사용자 등록(요청 완료후 삭제될거임 : stateless)
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		Authentication whatHaveYouGot = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("세션 임시 저장 Authentication 확인용 : " + whatHaveYouGot);

		//다음 필터 ㄱ
        filterChain.doFilter(request, response);

	}

}

/**
 * JWTFilter 클래스에서 Authorization 값의 쿠키를 찾아 처리한 뒤 filterChain에 넘기는 이유는 사용자의 권한과 인증 상태를 확인하여 요청을 처리할 수 있도록 하기 위해서
 * 
 * 필터체인이 실행되면, JWTFilter에서 JWT를 검증한 후, 사용자 인증 정보를 생성하고, 이를 SecurityContextHolder에 저장합니다.
 * 이후 요청 흐름 내에서 이 저장된 정보를 다른 보안 관련 작업에 재사용하구요.(컨트롤러에서 불러와서 model로 JSP에 전달 )
 * 요청이 완료되고 필터 체인이 종료될 때, SecurityContextHolder의 정보를 초기화 합니다.
 * */
