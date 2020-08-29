package io.github.positoy.oauthaccountboard;

public enum ResourceProvider {
    NAVER("NAVER"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    FACEBOOK("FACEBOOK"),
    UNKNOWN("UNKNOWN");


    private final String serviceName;

    ResourceProvider(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}