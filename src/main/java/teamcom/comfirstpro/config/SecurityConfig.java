package teamcom.comfirstpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests //처음 뜨는 시큐리티 로그인 화면 해제
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화

        http.formLogin(login -> login
                        .loginPage("/login") //보안이 필요한 페이지에 접근 시 login이라는 로그인 페이지에 리다이렉트
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login_process") //로그인이 수행되는 url
                        .defaultSuccessUrl("/") //성공 시 리다이렉트 될 기본주소
                        .failureForwardUrl("/login")); //실패 시 리다이렉트 될 기본주소
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(); //비밀번호 암호화
    }
}
