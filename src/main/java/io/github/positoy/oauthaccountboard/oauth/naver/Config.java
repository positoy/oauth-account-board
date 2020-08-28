package io.github.positoy.oauthaccountboard.oauth.naver;

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

        private KEY() {}
    }

    public class VAL {
        public static final String CLIENT_ID = "AlS3TCLxJYn7SNPp75LE";
        public static final String CLIENT_SECRET = "fCkaLjRvJK";
        public static final String STATE = "hello";
        public static final String SERVICE_PROVIDER = "NAVER";

        public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
        public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
        public static final String GRANT_TYPE_DELETE = "delete";

        private VAL() {}
    }
}
