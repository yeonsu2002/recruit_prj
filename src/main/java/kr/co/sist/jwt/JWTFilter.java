package kr.co.sist.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.entity.CustomUser;
import kr.bit.entity.Member;
import kr.bit.entity.Role;
/**
 * 스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 커스텀 필터를 등록해야 한다.
 * 해당 필터를 통해 요청 쿠키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성한다. 
 * (이 세션은 STATLESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸 된다.)
 * 
 * JWTFilter의 역할 = 쿠키 검증 -> 세션저장 
 * 
 * access 토큰과 refresh 토큰 모두 발급하는 [NEW] JWTFilter 작성
 */
public class JWTFilter extends OncePerRequestFilter {
	
	private final JWTUtil jwtUtil;
	
	public JWTFilter (JWTUtil jwtUtil) {
		
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println("JWTFilter 통과");

		// 헤더에서 Authorization키에 담긴 토큰을 꺼냄 
		String authorizationHeader = request.getHeader("Authorization");// 값: "Bearer access"
		String accessToken = null;
		
		//Authorization키의 토큰에서 "Bearer " 문자열을 제거, 서버에서 생성시 부가하고있음(CustomSuccessHandler 클래스에서)
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			//accessToken = authorizationHeader.substring(7); // "Bearer" 이후의 토큰만 추출 
			accessToken = authorizationHeader.replaceAll("Bearer ", ""); // "Bearer " 제거 
		}
		
		// 토큰이 없다면 다음 필터로 넘김 : 권한이 필요없는 요청도 있기때문에  
		if(accessToken == null) {
			
			filterChain.doFilter(request, response);
			
			return;
		}
		
		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음 
		try {
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {
			
			e.printStackTrace();
			
			// 혹은 HTTP상태코드, 메시지 반환 : 모든 JSP에서 401감지 후 access재발급 Post요청하는 ajax 작성 
		    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "access token expired");
		    
		    // 혹은 특정 JSP로 리다이렉트 : 해당 JSP에서 401감지 후 access재발급 Post요청하는 ajax 작성 
		    response.sendRedirect("/tokenExpired?error=expired");

		    
		    // 혹은 여기서 바로 request.getCookies()로 refresh를 검증하여 access토큰 재발급 
		    
/**			
			//아래 코드는 SPA에서 적절. JSP에 알맞지 않음 
			//response Body
			PrintWriter writer = response.getWriter(); // ??
			writer.print("Access token expired");
		    //response status code
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // ??
		    return;
*/		    
		    //	이 부분에서 사용자는 "Access token expired" 라는 예외처리 문구를 보게된다.
		    //	-> 401 Unauthorized를 감지하여 자동으로 로그인페이지로 리디렉션 하는 코드가 필요하다. 
		    //	방법 
		    // 1. 위 코드를 JSON응답으로 바꾸고, SPA(React, Vue) 에서 AJAX요청을 처리한다 (SPA에서 추천)
		    // 2. catch문에서 바로 response.sendRedirct("/login.jsp");를 한다? (JSP에서 추천)
		}
		
		// 토큰이 access인지, refresh인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtil.getCategory(accessToken);
		
		if(category == null || !category.equals("access")) {
			
			// 직접 리디렉트: SSR (Spring MVC, JSP, Thymeleaf)
			response.sendRedirect("/member/login"); // 401 에러 -> 로그인페이지 리디렉트 
/**		    
			//response body
		    PrintWriter writer = response.getWriter();
		    writer.print("invalid access token");
		    //response status code
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
*/		    
		}
		
		// username, role 값을 획득
		String username = jwtUtil.getUsername(accessToken);
		String role = jwtUtil.getRole(accessToken);
		
		Member member = new Member();
		member.setUsername(username);
		try {
		    Role roleEnum = Role.valueOf(role); //문자열을 Enum으로 변환하여 설정 
		    member.setRole(roleEnum);
		} catch (IllegalArgumentException e) {
		    throw new AuthenticationException("Invalid role: " + role); // Role Enum에 없는 경우, 명확한 예외 메시지를 출력
		} 
		
		CustomUser customUser = new CustomUser(member);
		
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authToken); //이 인증정보는 현재 요청(request)이 처리되는 동안 유지됨. 
		
		filterChain.doFilter(request, response);

	}

}
/** 
 * 	https://www.devyummi.com/page?id=669516f159f57d23e8a0b6af
 * 
 * 	access토큰은 API서버에서 요청마다 Authorization 헤더에서 토큰을 받아 검증한다.
 * 	refresh토큰은 자동으로 서버에 전송되므로 클라이언트 측에서 명시적으로 추가할 필요는 없다. 
 * 	
 * 	SecurityContextHolder는 기본적으로 ThreadLocal기반이므로, 요청처리가 끝나면 컨텍스트가 초기화됨. 
 * 	JWTFilter는 요청마다 SecurityContextHolder를 설정하지만, 이는 Stateless 방식이므로 유지되지는 않음 
 * 	세션을 사용하지 않아 서부 부하감소, 보안성이 높지만..
 * 	요청마다 JWT를 검증해야 하므로 성능 부담이 있음. 
 * 	Redis같은 캐시 시스템을 활용해 검증 결과를 저장하여 성능을 개선하는 방법도 있음. 
 * 
 * ********* 중 요 ****************
 * 	refresh토큰 검증 및 access 재발급 로직을 JWTFilter 에서 구성하는 것보다, 
 * 	401 Unauthorized시, 특정 JSP에 리디렉트 하게 해서 해당 JSP에 ajax를 사용하여 PostMapping("/reissue")로 컨트롤러에서 재발급하는 로직을 짜보고 
 *  아니면 컨트롤러에서 받고, 서비스쪽에서 로직을 구성하는 건 어떨까?
 *  각각의 장단점을 고민해보자. 
 */
