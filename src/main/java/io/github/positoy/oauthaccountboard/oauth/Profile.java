package io.github.positoy.oauthaccountboard.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Profile {
    String id;
    String name;
    String nickname;
    String profile_image;
}