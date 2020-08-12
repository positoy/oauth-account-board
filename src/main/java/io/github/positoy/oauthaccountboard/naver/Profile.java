package io.github.positoy.oauthaccountboard.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Profile {
    final public String id;
    final public String name;
    final public String nickname;
    final public String profile_image;
}