package io.github.positoy.oauthaccountboard.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Token {
    public String access_token;
    public String refresh_token;
    public String token_type;
    public String expires_in;
}