package io.github.positoy.oauthaccountboard;

public enum ResourceProvider {
    NAVER("NAVER"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    FACEBOOOK("FACEBOOK");

    private String serviceName;

    ResourceProvider(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}