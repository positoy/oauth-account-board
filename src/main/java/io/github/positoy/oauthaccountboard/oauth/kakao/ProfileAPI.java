package io.github.positoy.oauthaccountboard.oauth.kakao;

import io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager;
import io.github.positoy.oauthaccountboard.oauth.Profile;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileAPI {

    static final Logger logger = LoggerFactory.getLogger(ProfileAPI.class);

    static final String API_URL = "https://kapi.kakao.com/v2/user/me";

    private ProfileAPI() {}

    public static Profile get(String accessToken) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.POST);
        request.setRequestHeader("Authorization", "Bearer " + accessToken);
        request.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        JSONObject json = null;
        try {
            json = request.getJsonResponse();
        } catch (Exception e) {
            logger.info("failed to get and parse naver profile response");
            e.printStackTrace();
            return null;
        }

        String id = Long.toString((Long)json.get("id"));
        String nickname = (String)((JSONObject)json.get("properties")).get("nickname");
        String profile_image = "";
        String name = "";

        return (id == null) ? null : new Profile(id, name, nickname, profile_image);
    }
}
