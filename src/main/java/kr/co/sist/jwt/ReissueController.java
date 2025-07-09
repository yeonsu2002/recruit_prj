package kr.co.sist.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.jwt.JWTUtil;
import kr.bit.service.TokenService;

/**
 * 서버측 JWTFilter에서 Access 토큰의 만료로 인한 특정한 상태 코드가 응답되면 
 * 프론트측 Axios Interceptor와 같은 예외 핸들러에서 
 * Access 토큰 재발급을 위한 Refresh을 서버측으로 전송한다.
 * 이때 서버에서는 Refresh 토큰을 받아 새로운 Access 토큰을 응답하는 코드를 작성하면 된다.
 */

//@Controller
//@ResponseBody  
public class ReissueController {

	private final JWTUtil jwtUtil;
	
	private final TokenService tokenService;
	
	public ReissueController(JWTUtil jwtUtil, TokenService tokenService) {
		
		this.jwtUtil = jwtUtil;
		this.tokenService = tokenService;
	}
	
	@GetMapping("/tokenExpired")
	public String getTokenExpired(@RequestParam(value = "error", required = false) String error, Model model ) {
		model.addAttribute("error", error);
		
		return "error/tokenExpired";
	}
	
	//------------------------------------------------------------------------------------------

	/**
	 * 나중에 서비스와 컨트롤 분리해줘요 
	 */
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue (HttpServletRequest request, HttpServletResponse response){
		
        // 1. 쿠키에서 Refresh Token 가져오기
		String refresh = null;
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			
			if(cookie.getName().equals("refresh")) {
				
				refresh = cookie.getValue();
			}
		}
		/**
		 * 	JSP: 문자열(refresh token null)이 포함된 HTTP 응답을 받게된다. Ajax나 Fetch API로 처리해 
		 * 	CSR에서도 똑같다. 
		 */
		if(refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
		}

		try {
			// 새로운 access 토큰 발급 
			String newAccess = tokenService.reissueAccessToken(refresh);
			
			// 응답헤더에 새로운 access 토큰 추가
			response.addHeader("access", newAccess);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		
		//토큰이 refresh 인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtil.getCategory(refresh);
		
		if(!category.equals("refresh")) {
			
			//response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
		}
		
		String username = jwtUtil.getUsername(category);
		String role = jwtUtil.getRole(username);
		String name = jwtUtil.getName(refresh);
		
		// make new access
		String newAccess = jwtUtil.createJwt("access", username, role, name, 1000L*60*15);
		// make new refresh
		String newRefresh = jwtUtil.createJwt("refresh", username, role, name, 1000L*60*60*24*2);
		
		//response 
		response.addHeader("access", "Bearer " + newAccess);
		response.addCookie(createCookie("refresh", newRefresh));
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//내부에서 쓸 쿠키생성 메서드
	private Cookie createCookie(String key, String value) {
		
		Cookie cookie = new Cookie(key, value);
		
		cookie.setMaxAge(60*60*24*2); // 쿠키 48시간짜리 (refresh와 같아야 자동로그인 기능이 정상적으로 동작)  
		cookie.setSecure(false); //HTTPS 에서만 쿠키가 전송 : 개발환경에서는 false -> 네트워크 스닛핑 위험DOWN
		//cookie.setPath("/"); //모든 경로에서 쿠키를 전송, 기본 자동설정  
		cookie.setHttpOnly(true); // 자바스크립트에서 접근 불가 -> XSS 공격 방어 
		
		return cookie;
	}
	
	
	
}

/**
 *	-- Refresh Rotate --
 *	= access 갱신시 refresh도 함께 갱신 
 *	
 *	-- 추가 구현 작업 --
 *	= 발급했던 Refresh 토큰을 모두 기억한 뒤, Rotate 이전의 Refresh 토큰은 사용하지 못하도록 해야 함	 
 * 
 * 
 * 
 */
