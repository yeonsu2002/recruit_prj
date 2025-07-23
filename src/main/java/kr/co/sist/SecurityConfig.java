package kr.co.sist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.sist.admin.login.AdminDetailsServiceImpl;
import kr.co.sist.jwt.AdminCustomLoginFailureHandler;
import kr.co.sist.jwt.CustomLoginFailureHandler;
import kr.co.sist.jwt.CustomLoginSuccessHandler;
import kr.co.sist.jwt.JWTFIlter;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.login.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AdminDetailsServiceImpl adminDetailsServiceImpl;
    
    public SecurityConfig(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl, AdminDetailsServiceImpl adminDetailsServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.adminDetailsServiceImpl = adminDetailsServiceImpl;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 🔥 문제의 AuthenticationManager Bean 삭제 또는 수정
    // 각 필터체인에서 개별적으로 userDetailsService를 설정하도록 변경

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/admin_login", "/admin/css/**", "/admin/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .userDetailsService(adminDetailsServiceImpl)  // 🔥 직접 설정
            .formLogin(auth -> auth
                .loginPage("/admin/admin_login")
                .loginProcessingUrl("/admin/login_process")
                .usernameParameter("admin_email")
                .passwordParameter("admin_password")
                .failureHandler(new AdminCustomLoginFailureHandler())
                .defaultSuccessUrl("/admin/admin_mainpage",true)
                .permitAll()
            )
            .logout(auth -> auth
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/admin_login")
                .invalidateHttpSession(true)
            );
        
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/reissue").permitAll()
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .userDetailsService(userDetailsServiceImpl)  // 🔥 직접 설정
            .formLogin(auth -> auth
                .loginPage("/login")
                .loginProcessingUrl("/loginProcess")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler(new CustomLoginFailureHandler())
                .successHandler(new CustomLoginSuccessHandler(jwtUtil))
                .permitAll()
            )
            .logout(auth -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "access", "refresh")
            )
            .addFilterAfter(new JWTFIlter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}