package io.github.positoy.oauthaccountboard.model;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.naver.Profile;
import io.github.positoy.oauthaccountboard.naver.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Account {
    int id;
    ResourceProvider resourceProvider;
    Token token;
    Profile profile;
}
