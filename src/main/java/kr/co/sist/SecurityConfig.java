package kr.co.sist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.sist.jwt.CustomLoginFailureHandler;
import kr.co.sist.jwt.CustomLoginSuccessHandler;
import kr.co.sist.jwt.JWTFIlter;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.login.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private final JWTUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	
	public SecurityConfig(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
		this.jwtUtil = jwtUtil;
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}
	
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //filter 체인 설정 : 로그인 창을 제공하지 않도록 설정
    //모든 요청에 대한 인증 없이 사용가능하도록 설정 (추후에 수정 필요)
    http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
    //csrf : 접속자의 연결 정보(세션)를 가로채서 접속하는 것
    .csrf(csrf -> csrf.disable()); // CSRF 토큰 검사 비활성화 
    //시큐리티의 http.formLogin 안쓰겠다는 것 (삭제하면 정상작동 됨)
    //.formLogin(login -> login.disable());
    
    
    //시큐리티 자동 로그인 처리 (일반회원, 기업회원에 한해서)
    http
    	.formLogin(auth -> auth
    			.loginPage("/login")
    			.loginProcessingUrl("/loginProcess") //post 해야해!
    			.usernameParameter("email") //이거 까먹고 겁나 시간 잡아먹었네, 아이디는 username으로 해줘야 편하긴 해 
    			.passwordParameter("password")
    			.failureHandler(new CustomLoginFailureHandler()) //로그인 실패 핸들러 
    			.successHandler(new CustomLoginSuccessHandler(jwtUtil)) //JWT 발급 핸들러
    			.permitAll()
			);
     
    
    //시큐리티 자동 로그아웃 처리
    http
    	.logout(auth -> auth
    			.logoutUrl("/logout")
    			.logoutSuccessUrl("/")
    			.invalidateHttpSession(true)
    			.deleteCookies("JSESSIONID", "Authorization") //JWT쿠키도삭제함 
			);
    
    //JWTFilter는 로그인 성공후에 불러올거야.. (JWT발급때문에 )
    http
	    //발급된 JWT를 요청마다 검증하고 세션처럼 등록해주는 인증 필터 
	    .addFilterAfter(new JWTFIlter(jwtUtil), UsernamePasswordAuthenticationFilter.class); 
    
    return http.build();
  } //filterChain

} //class
