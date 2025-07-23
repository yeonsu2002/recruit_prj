package kr.co.sist.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.repository.SelfIntroductionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@ResponseBody
public class ReissueController {

	private final JWTUtil jwtUtil;
	
	@PostMapping("/reissue")
	public ResponseEntity<?> reissueAccessToken(HttpServletRequest request, HttpServletResponse response){
		
		// refreshToken 가져와
		String refresh = "";
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("refresh")) {
				refresh = cookie.getValue();
			}
		}
		
		//refresh 없으면?
		if(refresh == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("--- refresh Token is null ---"); //BAD_REQUEST : 400에러 발생시킴
		}
		
		//refresh 토큰 만료 검증
		try {
			jwtUtil.isExpired(refresh);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("--- refresh Token is expired :" + e);
		}
		
		//토큰이 refresh인지 확인
		String category = jwtUtil.getCategory(refresh);
		if(!category.equals("refresh")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" The category is not a refresh! ");
		}
		
		UserDTO uDTO = new UserDTO();
		uDTO.setEmail( jwtUtil.getEmail(refresh));
		uDTO.setCorpNo( jwtUtil.getCorpNo(refresh));
		uDTO.setName( jwtUtil.getName(refresh));
		uDTO.setRole(jwtUtil.getRole(refresh));
		
		//새로운 access jwt 만들기 
		String newAccess = jwtUtil.createJwt("access", uDTO, 60 * 10 * 1000L); //10분짜리 new access토큰 
		
		//응답에 다시 붙여주기
		response.addHeader("access", newAccess);
		
		return ResponseEntity.ok("access 토큰 재발급 성공");
	}
	
}
/**
 * 클라이언트에서 서버로 필요한 데이터를 요청하기 위해 JSON 데이터를 요청 본문에 담아서 서버로 보내면, 
 * 서버에서는 @RequestBody 어노테이션을 사용하여 HTTP 요청 본문에 담긴 값들을 자바객체로 변환시켜, 객체에 저장한다.
 * 서버에서 클라이언트로 응답 데이터를 전송하기 위해
 * @ResponseBody 어노테이션을 사용하여 자바 객체를 HTTP 응답 본문의 객체로 변환하여 클라이언트로 전송한다. 
 * */
