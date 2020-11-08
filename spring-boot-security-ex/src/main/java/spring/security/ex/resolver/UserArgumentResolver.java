package spring.security.ex.resolver;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import spring.security.ex.annotation.SocialUser;
import spring.security.ex.domain.User;
import spring.security.ex.domain.UserRepository;
import spring.security.ex.domain.enums.SocialType;

@RequiredArgsConstructor
@Component
public class UserArgumentResolver implements
        HandlerMethodArgumentResolver { // 전략패턴으로 구현되어 있으며 컨트롤러 메서드에서 특정 조건에 해당하는 파라미터가 있으면 생성한 로직을 처리한 후 해당 파라미터에 바인딩 해주는 전략 인터페이스

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) { // 해당하는 파라미터를 지원할지 여부를 반환
        // supportsParameter()의 특징은 처음 한 번 체크된 부분은 캐시되어 동일한 호출 시에는 체크되지 않고 캐시된 결과를 반환한다.
        return parameter.getParameterAnnotation(SocialUser.class) != null &&
                parameter.getParameterType().equals(User.class); // @SocialUser 어노테이션이 있고 타입이 User인 파라미터만 true를 반환
    }

    // 검증이 완료된 파라미터 정보를 받는다.
    // 현재는 검증이 되어 세션에 해당하는 User가 있다면 반환하고 없다면 User를 생성한다.
    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) { // 파라미터의 인자값에 대한 정보를 바탕으로 실제 객체를 생성하여 해당 파라미터 객체를 바인
        final HttpSession session = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getSession();
        final User user = (User)session.getAttribute("user");

        return getUser(user, session);
    }

    // 인증된 User 객체를 만드는 메서드
    public Object getUser(User user, final HttpSession session) {
        if (user == null) {
            try {
                // 2.0부터는 기존의 OAuth2Authentication이 아닌 엑세스 토큰까지 제공한다는 의미에서 OAuth2AuthenticationToken을 제공한다.
                final OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken)SecurityContextHolder.getContext()
                        .getAuthentication();
                final Map<String, Object> map = authentication.getPrincipal().getAttributes();
                // 예전에는 getAuthorities로 권한을 불러와 소셜미디어 확인을 했다면 이제는 getAuthorizedClientRegistrationId를 통해 파악이 가능하다.
                final User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);

                user = userRepository.findByEmail(convertUser.getEmail());

                if (user == null) {
                    user = userRepository.save(convertUser);
                }

                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);

            } catch (final ClassCastException e) {
                return user;
            }
        }
        return user;
    }

    // 인증된 소셜 미디어 타입에 따라 User 생성
    private User convertUser(final String authority, final Map<String, Object> map) {
        if (SocialType.FACEBOOK.isEqual(authority)) {
            return getModernUser(SocialType.FACEBOOK, map);
        } else if (SocialType.GOOGLE.isEqual(authority)) {
            return getModernUser(SocialType.GOOGLE, map);
        } else if (SocialType.KAKAO.isEqual(authority)) {
            return getKakao(SocialType.KAKAO, map);
        }

        return null;
    }

    public User getModernUser(final SocialType socialType, final Map<String, Object> map) {
        return User.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .principal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public User getKakao(final SocialType socialType, final Map<String, Object> map) {
        final Map<String, String> propertyMap = (Map<String, String>)map.get("properties");
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(propertyMap.get("kaccount_email"))
                .principal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .build();
    }

    // authentication이 권한을 갖고 있는지 확인하는 용도로 저장된 User 권한이 없으면 SecurityContextHolder를 사용하여 해당 소셜 미디어타입으로 권한을 저장
    private void setRoleIfNotSame(final User user, final OAuth2AuthenticationToken authentication,
            final Map<String, Object> map) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A",
                    AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
        }
    }
}
