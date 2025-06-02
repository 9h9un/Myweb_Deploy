package com.example.admin_board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Spring Security 기본 보안 설정 Bean 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 모든 요청에 대해 인증 없이 접근 허용
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll()
                )
                // CSRF 보호 비활성화 (주로 테스트 목적)
                .csrf().disable();

        return http.build();
    }
}
