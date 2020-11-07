package spring.boot.security.ex.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import lombok.RequiredArgsConstructor;
import spring.boot.security.ex.domain.enums.SocialType;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 기능 사용
@EnableOAuth2Client // OAuth2 기능 사
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 요청, 권한, 기타 설정에 대해서 필수적으로 최적화한 설정을 위해 상속

    // OAuth2 AccessToken이 들어있는 곳
    private final OAuth2ClientContext oAuth2ClientContext;

    //@formatter:off
    @Override
    protected void configure(final HttpSecurity http) throws Exception { // 요청, 권한, 기타 설정에 대한 필수적 설정
        // 문자 인코딩 설정
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();

        http
                .authorizeRequests()
                // 요청을 패턴 형식으로 정해 누구나 접근할 수 있도록 설정
                .antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                // 위에서 설정한 패턴을 제외한 나머지는 인증된 사용자만 사용 가능
                .anyRequest().authenticated()
            .and()
                // 응답 헤더
                .headers()
                // XFrameOptionsHeaderWriter의 최적화 설정을 허용하지 않음.
                .frameOptions().disable()
            .and()
                .exceptionHandling()
                // 인증되지 않은 사용자가 허용되지 않은 리소스를 접근할 경우 "/login" 경로로 이동
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
                // 로그인 폼
                .formLogin()
                // 성공시 해당 Url로 이동
                .successForwardUrl("/board/list")
            .and()
                // 로그아웃 설정
                .logout()
                // 로그아웃 Url
                .logoutUrl("/logout")
                // 로그아웃 성공 Url
                .logoutSuccessUrl("/")
                // 로그아웃 시 해당 쿠기 삭제
                .deleteCookies("JSESSIONID")
                // 설정된 섹션의 무효화
                .invalidateHttpSession(true)
            .and()
                // 첫번째 인자보다 먼저 시작될 필터
                .addFilterBefore(filter, CsrfFilter.class)
                .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)
                // csrf 사용 하지 않는다는 의미
                .csrf().disable();
    }
    //@formatter:on

    // OAuth21ClientContextFilter를 불러와서 올바른 순서로 필터가 동작하도록 설정한다.
    // 스프링 시큐리티 필터가 실행되기 전에 충분히 낮은 순서로 필터를 등록해야 한다.
    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(final OAuth2ClientContextFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);

        return registrationBean;
    }

    // 각 소셜 미디어 타입을 받아 필터를 설정
    // 각 소셜 미디어 필터를 리스트 형식으로 한꺼전에 설정
    private Filter oauth2Filter() {
        final CompositeFilter filter = new CompositeFilter();
        final List<Filter> filters = new ArrayList<>();

        filters.add(oauth2Filter(facebook(), "/login/facebook", SocialType.FACEBOOK));
        filters.add(oauth2Filter(google(), "/login/google", SocialType.GOOGLE));
        filters.add(oauth2Filter(kakao(), "/login/kakao", SocialType.KAKAO));
        filter.setFilters(filters);

        return filter;
    }

    private Filter oauth2Filter(final ClientResources client, final String path, final SocialType socialType) {
        // 인증이 수행 될 경로를 넣어 OAuth2 클라이언트 인증 처리 필터를 생성
        final OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        // 권한 서버와의 통신을 위해 생성하며, client 프로퍼티와 OAuth2ClientContext가 필요.
        final OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oAuth2ClientContext);

        filter.setRestTemplate(template);
        // User의 권한을 최적화해서 생성하고자 UserInfoTokenServices를 상속받은 UserTokenService를 생성하여 OAuth2 AccessToken 검증을 위해 생성한 UserTokenService를 필터의 토큰 서비스 등록
        filter.setTokenServices(new UserTokenService(client, socialType));
        // 성공하면 리다이렉트 URL
        filter.setAuthenticationSuccessHandler(((request, response, authentication) -> response.sendRedirect(
                "/" + socialType.getName() + "/complete")));
        // 실패하면 리다이렉트 URL
        filter.setAuthenticationFailureHandler(((request, response, exception) -> response.sendRedirect("/error")));

        return filter;
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("kakao")
    public ClientResources kakao() {
        return new ClientResources();
    }
}



