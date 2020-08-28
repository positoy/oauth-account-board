package io.github.positoy.oauthaccountboard.oauth.kakao;

public class Config {

    public class KEY {
        public static final String GRANT_TYPE = "grant_type";
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET = "client_secret";
        public static final String CODE = "code";
        public static final String STATE = "state";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String SERVICE_PROVIDER = "service_provider";
        public static final String REDIRECT_URI = "redirect_uri";

        public static final String AUTHORIZATION = "Authorization";

        private KEY() {}
    }

    public class VAL {
        public static final String CLIENT_ID = "918e3d5257c0e3b80e1a94f9130ab08d";
        public static final String CLIENT_SECRET = "9FuCZhigT24BMiVQDMfpV3mZHD5enH2G";
        public static final String STATE = "hello";
        public static final String SERVICE_PROVIDER = "KAKAO";

        public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
        public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
        public static final String GRANT_TYPE_DELETE = "delete";
        public static final String REDIRECT_URI = "http%3A%2F%2Flocalhost%2Flogin%2Fkakao";

        private VAL() {}
    }
}
