package io.github.positoy.oauthaccountboard.oauth.kakao;


import io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager;
import io.github.positoy.oauthaccountboard.oauth.Token;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenAPI {

    static final Logger logger = LoggerFactory.getLogger(AccessTokenAPI.class);

    static final String API_URL = "https://kauth.kakao.com/oauth/token";
    static final String API_URL_UNLINK = "https://kauth.kakao.com/v1/user/unlink";

    private AccessTokenAPI() {
    }

    private static Map<String,String> getCommonQueryParams() {
        Map<String,String> params = new HashMap<>();
        params.put(Config.KEY.CLIENT_ID, Config.VAL.CLIENT_ID);
        params.put(Config.KEY.CLIENT_SECRET, Config.VAL.CLIENT_SECRET);
        return params;
    }

    public static Token get(String authcode) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.POST);
        request.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String,String> queryParams = getCommonQueryParams();
        queryParams.put(Config.KEY.GRANT_TYPE, Config.VAL.GRANT_TYPE_AUTHORIZATION_CODE);
        queryParams.put(Config.KEY.CODE, authcode);
        queryParams.put(Config.KEY.REDIRECT_URI, Config.VAL.REDIRECT_URI);
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
        String expires_in = Long.toString((Long)json.get("expires_in"));

        return (access_token == null) ? null : new Token(access_token, refresh_token, token_type, expires_in);
    }

    public static Token refresh(String refreshtoken) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.POST);
        request.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String,String> queryParams = getCommonQueryParams();
        queryParams.put(Config.KEY.GRANT_TYPE, Config.VAL.GRANT_TYPE_REFRESH_TOKEN);
        queryParams.put(Config.KEY.REFRESH_TOKEN, refreshtoken);
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
        HTTPRequestManager request = new HTTPRequestManager(API_URL_UNLINK, HTTPRequestManager.METHOD.POST);
        request.setRequestHeader(Config.KEY.AUTHORIZATION, "Bearer " + access_token);
        request.setQueryParam(Config.KEY.AUTHORIZATION, "Bearer " + access_token);

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
