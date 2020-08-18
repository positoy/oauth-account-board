package io.github.positoy.oauthaccountboard.models;

import io.github.positoy.oauthaccountboard.oauth.Profile;
import io.github.positoy.oauthaccountboard.oauth.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
public class AccountSession extends Account{
    Token token;
    Profile profile;

    public AccountSession(Account account, Token token, Profile profile) {
        super(account);
        this.token = token;
        this.profile = profile;
    }
}
