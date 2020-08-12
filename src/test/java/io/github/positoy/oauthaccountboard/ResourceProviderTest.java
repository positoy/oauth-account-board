package io.github.positoy.oauthaccountboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceProviderTest {

    @Test
    public void doInitializationTest() {
        ResourceProvider naver = ResourceProvider.NAVER;
        ResourceProvider kakao = ResourceProvider.KAKAO;
        ResourceProvider google = ResourceProvider.GOOGLE;
        ResourceProvider facebook = ResourceProvider.FACEBOOOK;

        Assertions.assertEquals(naver.getServiceName(), "NAVER");
        Assertions.assertEquals(kakao.getServiceName(), "KAKAO");
        Assertions.assertEquals(google.getServiceName(), "GOOGLE");
        Assertions.assertEquals(facebook.getServiceName(), "FACEBOOK");
    }

}