package spring.security.ex.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import spring.boot.security.ex.domain.enums.SocialType;

// User 정보를 얻기 위해 소셜 서버와 통신하는 역할
public class UserTokenService extends UserInfoTokenServices {

    public UserTokenService(final ClientResources resources, final SocialType socialType) {
        // 통신을 위해 clientId, URI가 필요하다.
        super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
        setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType));
    }

    // 인증된 사용자의 인가에 대한 권한을 설정하는 곳이다.
    public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor {

        private final String sociType;

        public OAuth2AuthoritiesExtractor(final SocialType socialType) {
            this.sociType = socialType.getRoleType();
        }

        @Override
        public List<GrantedAuthority> extractAuthorities(final Map<String, Object> map) {
            return AuthorityUtils.createAuthorityList(this.sociType);
        }
    }
}
