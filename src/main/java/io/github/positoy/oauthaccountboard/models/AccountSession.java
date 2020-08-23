package io.github.positoy.oauthaccountboard.models;

import io.github.positoy.oauthaccountboard.oauth.Profile;
import io.github.positoy.oauthaccountboard.oauth.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@ToString
public class AccountSession extends Account{
    Token token;
    Profile profile;

    public AccountSession(Account account, Token token, Profile profile) {
        super(account);
        this.token = token;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AccountSession that = (AccountSession) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, profile);
    }
}
