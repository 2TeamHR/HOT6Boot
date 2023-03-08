package com.hotsix.titans.config;

import com.hotsix.titans.jwt.JwtAccessDeniedHandler;
import com.hotsix.titans.jwt.JwtAuthenticationEntryPoint;
import com.hotsix.titans.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    public SecurityConfig(TokenProvider tokenProvider
            , JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
            , JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    /* 1. 암호화 처리를 위한 PasswordEncoder를 빈으로 설정(빈을 등록 시 메소드 이름 오타 없을 것) */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 2. 시큐리티 설정을 무시 할 정적 리소스 등록 */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/js/**", "/images/**",
                "/lib/**", "/productimgs/**");
    }

    /* 3. HTTP요청에 대한 권한별 설정(세션 인증 -> 토큰 인증으로 인해 바뀐 부분 존재) */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .exceptionHandling()

                /* 기본 시큐리티 설정에서 JWT 토큰과 관련된 유효성과 권한 체크용 설정 */
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)    // 유효한 자격 증명 없을 시(401)
                .accessDeniedHandler(jwtAccessDeniedHandler)            // 필요한 권한 없이 접근 시(403)
                .and()
                .authorizeRequests()
//		    	.antMatchers("/").authenticated()
		    	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/ea/**").permitAll()
		    	.antMatchers("/auth/**").permitAll()
				.antMatchers("/api/v1/annual/**").permitAll()
		    	.antMatchers("/api/v1/reviews/**").permitAll()
				.antMatchers("/api/v1/members/**").permitAll()	//@@ 페이지 권한
				.antMatchers("/auth/signup/**").permitAll()	//@@ 신규 사원 등록
				.antMatchers("/api/v1/mypage/**").permitAll()
				.antMatchers("/api/v1/salary/**").permitAll()
//		    	.antMatchers("/api/**").hasRole("MEMBER")
//		    	.antMatchers("/api/**").hasRole("ADMIN")
				.antMatchers("/api/**").hasAnyRole("MEMBER", "ADMIN")
		    	.anyRequest().permitAll()	// 어떤 요청이든 허용 가능, 시큐리티를 활용한 로그인이 모두 완성 되지 않았을 때 활용할 것

                .and()

                /* 세션 인증 방식을 쓰지 않겠다는 설정 */
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()

                /* jwt 토큰 방식을 쓰겠다는 설정 */
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }

    /* 4. CORS 설정용 Bean(허용 할 origin과 httpMethod 종류와 header 값) */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Content-type"
                , "Access-Control-Allow-Headers", "Authorization"
                , "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}




