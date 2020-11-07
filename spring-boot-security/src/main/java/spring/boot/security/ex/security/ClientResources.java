package spring.boot.security.ex.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {
    @NestedConfigurationProperty // 해당 필드가 단일값이 아닌 중복 바인딩 가능하도록 함.
    private final AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails(); // application.yml에 있는 client 하위 key-value 값 매핑

    @NestedConfigurationProperty
    private final ResourceServerProperties resource = new ResourceServerProperties(); // OAuth2 리소스값 매핑하는데 사용하며, userInfoUri 값을 받는데 사용

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
