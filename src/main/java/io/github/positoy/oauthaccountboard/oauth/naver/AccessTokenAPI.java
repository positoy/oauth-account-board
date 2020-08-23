package io.github.positoy.oauthaccountboard.oauth.naver;


import io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager;
import io.github.positoy.oauthaccountboard.oauth.Token;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenAPI {

    static final Logger logger = LoggerFactory.getLogger(AccessTokenAPI.class);

    static final String API_URL = "https://nid.naver.com/oauth2.0/token";

    private AccessTokenAPI() {
    }

    private static Map<String,String> getCommonQueryParams() {
        Map<String,String> params = new HashMap<>();
        params.put(NaverConfiguration.KEY.CLIENT_ID, NaverConfiguration.VAL.CLIENT_ID);
        params.put(NaverConfiguration.KEY.CLIENT_SECRET, NaverConfiguration.VAL.CLIENT_SECRET);
        return params;
    }

    public static Token get(String authcode) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.GET);

        Map<String,String> queryParams = getCommonQueryParams();
        queryParams.put(NaverConfiguration.KEY.GRANT_TYPE, NaverConfiguration.VAL.GRANT_TYPE_AUTHORIZATION_CODE);
        queryParams.put(NaverConfiguration.KEY.CODE, authcode);
        queryParams.put(NaverConfiguration.KEY.STATE, NaverConfiguration.VAL.STATE);
        request.setQueryParams(queryParams);

        JSONObject json = null;
        try {
            json = request.getJsonResponse();
        } catch (Exception e) {
            logger.info("failed to get and parse accesstoken retrieve response");
            e.printStackTrace();
            return null;
        }

        String access_token = (String)json.get("access_token");
        String refresh_token = (String)json.get("refresh_token");
        String token_type = (String)json.get("token_type");
        String expires_in = (String)json.get("expires_in");

        return (access_token == null) ? null : new Token(access_token, refresh_token, token_type, expires_in);
    }

    public static Token refresh(String refreshtoken) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.GET);

        Map<String,String> queryParams = getCommonQueryParams();
        queryParams.put(NaverConfiguration.KEY.GRANT_TYPE, NaverConfiguration.VAL.GRANT_TYPE_REFRESH_TOKEN);
        queryParams.put(NaverConfiguration.KEY.REFRESH_TOKEN, refreshtoken);
        request.setQueryParams(queryParams);

        JSONObject json = null;
        try {
            json = request.getJsonResponse();
        } catch (Exception e) {
            logger.info("failed to get and parse refreshtoken response");
            e.printStackTrace();
            return null;
        }

        String access_token = (String)json.get("access_token");
        String refresh_token = (String)json.get("refresh_token");
        String token_type = (String)json.get("token_type");
        String expires_in = (String)json.get("expires_in");

        return new Token(access_token, refresh_token, token_type, expires_in);
    }

    public static boolean delete(String access_token) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.GET);

        Map<String,String> queryParams = getCommonQueryParams();
        queryParams.put(NaverConfiguration.KEY.GRANT_TYPE, NaverConfiguration.VAL.GRANT_TYPE_DELETE);
        queryParams.put(NaverConfiguration.KEY.ACCESS_TOKEN, access_token);
        queryParams.put(NaverConfiguration.KEY.SERVICE_PROVIDER, NaverConfiguration.VAL.SERVICE_PROVIDER);
        request.setQueryParams(queryParams);

        try {
            String response = request.getResponse();
            logger.info(response);
        } catch (Exception e) {
            logger.info("failed to get delete token response");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
