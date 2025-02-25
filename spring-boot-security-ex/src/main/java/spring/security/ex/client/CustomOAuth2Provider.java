package spring.security.ex.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum CustomOAuth2Provider {
    KAKAO {
        @Override
        public ClientRegistration.Builder getBuilder(final String registrationId) {
            final ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST,
                    DEFAULT_LOGIN_REDIRECT_URL);

            builder.scope("profile");
            builder.authorizationUri("https://kauth.kakao.com/oauth/token");
            builder.tokenUri("https://kauth.kakao.com/oauth/token");
            builder.userInfoUri("https://kauth.kakao.com/oauth/token");
            builder.userNameAttributeName("id");
            builder.clientName("Kakao");

            return builder;
        }
    };

    private static final String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";

    protected final ClientRegistration.Builder getBuilder(final String registrationId,
            final ClientAuthenticationMethod method,
            final String redirectUri) {
        final ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);

        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUriTemplate(redirectUri);

        return builder;
    }

    public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
