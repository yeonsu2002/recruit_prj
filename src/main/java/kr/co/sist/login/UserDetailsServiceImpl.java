package kr.co.sist.login;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.sist.jwt.CustomUser;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.UserEntity;

/**
 *  Spring Security가 로그인 처리 시 의존하는 핵심 서비스! 자동으로 호출됨 (UserDetailsService 객체로써)
 *  아이디/비밀번호 폼 로그인 처리
 *  
 *  이 클래스는 
 * 	SecurityConfig클래스의 
 * 		http
			.formLogin((auth) -> auth
				.loginPage("/member/login")
				.loginProcessingUrl("/member/loginProc")
				.defaultSuccessUrl("/", true) //로그인 성공시 해당 페이지로 가겠다는 의미 
			);
		코드 중 loginProcessingUrl에 의해 자동 호출된다. 
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository; 
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findById(email)
															.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
		
		UserDTO userDTO = UserDTO.from(userEntity);
		
		return new CustomUser(userDTO); //CustomUser를 Entity로 만들면 형변환은 필요없지만..
	}

}
