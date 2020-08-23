package io.github.positoy.oauthaccountboard.oauth.naver;

import io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager;
import io.github.positoy.oauthaccountboard.oauth.Profile;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileAPI {

    static final Logger logger = LoggerFactory.getLogger(ProfileAPI.class);

    static final String API_URL = "https://openapi.naver.com/v1/nid/me";

    private ProfileAPI() {}

    public static Profile get(String accessToken) {
        HTTPRequestManager request = new HTTPRequestManager(API_URL, HTTPRequestManager.METHOD.GET);
        request.setRequestHeader("Authorization", "Bearer " + accessToken);

        JSONObject json = null;
        try {
            json = request.getJsonResponse();
        } catch (Exception e) {
            logger.info("failed to get and parse naver profile response");
            e.printStackTrace();
            return null;
        }

        JSONObject obj = (JSONObject)json.get("response");
        String id = (String)obj.get("id");
        String nickname = (String)obj.get("nickname");
        String profile_image = (String)obj.get("profile_image");
        String name = (String)obj.get("name");

        return (id == null) ? null : new Profile(id, name, nickname, profile_image);
    }
}
