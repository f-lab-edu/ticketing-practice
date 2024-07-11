package com.ticketingberry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity	// 요청하는 모든 url이 Security를 통과시키게 만듦
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Bean
	// 여러가지 filter를 chain 방식으로 연결
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequest) -> 
		// 모든 주소("/**")에 대해 permitAll()
		// SessionCreationPolicy.ALWAYS: 세션을 항상 유지
		// csrf.ignoringRequestMatchers(): csrf를 쓰지 않을 것임
		
		// CSR이란?
		// 웹 사이트 공격 중 CSR 공격이 있음
		// CSR 공격: 'client'와 'web server' 사이에 다른 submit이 들어옴
		// 해결 방안: submit 할 때 토큰을 같이 주어야 함 (CSR 토큰정책) -> 안 쓸거임
		authorizeHttpRequest.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
		.formLogin((formLogin) -> formLogin.loginPage("/api/user/login").defaultSuccessUrl("/main"))
		.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/main").invalidateHttpSession(true));
		return http.build();
	}
	
	@Bean
	// 비밀번호 암호화 등록
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}











