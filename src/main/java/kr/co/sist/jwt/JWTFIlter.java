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
import kr.co.sist.user.dto.UserDTO;

/**
 * JWTFilter역할 = 쿠키 검증 -> 세션생성 -> 세션에 유저정보 저장 
 * SecurityContextholder에 세션을 생성하는데, 이 세션은 Stateless상태로 관리되기 때문에 해당 요청이 끝나면 소멸됨.
 */

public class JWTFIlter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	
	public JWTFIlter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = null;
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length == 0) {
			filterChain.doFilter(request, response);
			return;
		}
		
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("Authorization")) {
				authorization = cookie.getValue();
			}
		}
		String token = authorization; //사용자 정보 보유중 
		
		//1.쿠키 Authorizaiton의 값 검증
		if(token == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//2.쿠키 Authorization이 null이 아닐 때 -> 토큰 소멸시간 검증
		if(jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//3.쿠키가 정상이니 SecurityContextHolder세션에 저장하자, principal에 들어갈 것들: (CustomUser 클래스 생성 -> Authentication 인터페이스의 구현체인 UsernamePasswordAuthenticationToken 객체로 생성 )
		String username = jwtUtil.getEmail(token);
		String role = jwtUtil.getRole(token); //우리는 계정당 Role이 하나 
		String name = jwtUtil.getName(token);
		Long corpNo = jwtUtil.getCorpNo(token);
		
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(username);
		userDTO.setRole(role);
		userDTO.setName(name);
		userDTO.setCorpNo(corpNo);
		
		//UserDetails 구현체에 로그인한 회원정보 객체 담기
		CustomUser coustomUser = new CustomUser(userDTO);
		
		//스프링 시큐리티 인증토큰을 생성하기 (매개변수 = UserDetails 구현객체, 비밀번호(보안상 null 대입), 권한) 
		Authentication authToken = new UsernamePasswordAuthenticationToken(coustomUser, null, coustomUser.getAuthorities());
		
		//SecurityContextHolder 세션에 사용자 등록 (요청 완료후 자동 삭제됨 => stateless)
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		//완료됐어. 다음 필터 넘어가. 
		filterChain.doFilter(request, response);
		
		
	}

	
}
