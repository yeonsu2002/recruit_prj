package kr.co.sist.jwt;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import kr.bit.jwt.JWTUtil;
import kr.co.sist.user.dto.UserDTO;

//@Service
public class TokenService {

	private final JWTUtil jwtUtil;
	
	public TokenService(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	public String reissueAccessToken(String refreshToken) {
		
		// 1.토큰만료체크
		if(jwtUtil.isExpired(refreshToken)) {
			
			throw new RuntimeException("Refresh token expired");
		} 
		
		
		return null;
	}
	
	public String createJwt(UserDTO uDTO, Long expiredMs) {
  	String token = jwtUtil.createJwt(uDTO, expiredMs);
	}
	
}
