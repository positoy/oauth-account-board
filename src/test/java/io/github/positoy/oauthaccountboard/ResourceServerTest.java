package io.github.positoy.oauthaccountboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServerTest {

    @Test
    public void doInitializationTest() {
        ResourceServer naver = ResourceServer.NAVER;
        ResourceServer kakao = ResourceServer.KAKAO;
        ResourceServer google = ResourceServer.GOOGLE;
        ResourceServer facebook = ResourceServer.FACEBOOOK;

        Assertions.assertEquals(naver.getServiceName(), "NAVER");
        Assertions.assertEquals(kakao.getServiceName(), "KAKAO");
        Assertions.assertEquals(google.getServiceName(), "GOOGLE");
        Assertions.assertEquals(facebook.getServiceName(), "FACEBOOK");
    }

}