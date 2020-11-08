package spring.security.ex.security;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import lombok.RequiredArgsConstructor;
import spring.security.ex.client.CustomOAuth2Provider;
import spring.security.ex.domain.enums.SocialType;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 기능 사용
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 요청, 권한, 기타 설정에 대해서 필수적으로 최적화한 설정을 위해 상속

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
                .antMatchers("/bacebook").hasAuthority(SocialType.FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(SocialType.GOOGLE.getRoleType())
                .antMatchers("/kakao").hasAuthority(SocialType.KAKAO.getRoleType())
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
                // csrf 사용 하지 않는다는 의미
                .csrf().disable();
    }
    //@formatter:on

    // 인증 정보를 추가한다.
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            final OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${custom.oauth2.kakao.spring.security.ex.client-id") final String kakaClientId) {

        final List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaClientId)
                .clientSecret("test") // 필요없지만 null이면 실행이 되지 않음
                .jwkSetUri("test") // 필요없지만 null이면 실행이 되지 않음
                .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    // 구글, 페이스북의 인증 정보 빌드
    private ClientRegistration getRegistration(final OAuth2ClientProperties clientProperties, final String client) {
        if ("google".equals(client)) {
            final OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("goolge");

            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        } else if ("facebook".equals(client)) {
            final OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");

            CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri(
                            "https://graph.facebook.com/me?fields=id,name,email,link") // 페이스 북의 GraphAPI의 경우 scope()로는 필요한 필드 반환해주지 않기 때문에 직접 id, name, email, link 등을 파라미터로 넣어 요청하도록 설정
                    .scope("email")
                    .build();
        }

        return null;
    }
}



