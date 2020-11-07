package spring.boot.security.ex.domain.enums;

public enum SocialType {
    FACEBOOK("faceobook"),
    GOOGLE("google"),
    KAKAO("kakao");

    private static final String ROLE_PREFIX = "ROLE";
    private final String name;

    SocialType(final String name) {
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public boolean isEqual(final String authority) {
        return this.getRoleType().equals(authority);
    }
}
