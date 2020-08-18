package io.github.positoy.oauthaccountboard.oauth.naver;

import io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager;
import io.github.positoy.oauthaccountboard.oauth.Profile;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileAPI {

    final static Logger logger = LoggerFactory.getLogger(ProfileAPI.class);

    final static String apiURL = "https://openapi.naver.com/v1/nid/me";

    public static Profile get(String access_token) {
        HTTPRequestManager request = new HTTPRequestManager(apiURL, HTTPRequestManager.METHOD.GET);
        request.setRequestHeader("Authorization", "Bearer " + access_token);

        JSONObject json = null;
        try {
            json = request.getJsonResponse();
        } catch (Exception e) {
            logger.info("failed to get and parse naver profile response" + e.getMessage());
            e.printStackTrace();
            return null;
        }

        String id = (String) ((JSONObject)json.get("response")).get("id");
        String nickname = (String) ((JSONObject)json.get("response")).get("nickname");
        String profile_image = (String) ((JSONObject)json.get("response")).get("profile_image");
        String name = (String) ((JSONObject)json.get("response")).get("name");

        return (id == null) ? null : new Profile(id, name, nickname, profile_image);
    }
}
