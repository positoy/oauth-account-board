package io.github.positoy.oauthaccountboard;

public enum ResourceServer {
    NAVER("NAVER"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    FACEBOOOK("FACEBOOK");

    private String serviceName;

    ResourceServer(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}