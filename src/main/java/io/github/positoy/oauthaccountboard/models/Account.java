package io.github.positoy.oauthaccountboard.models;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.login.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@ToString
public class Account {
    int id;
    ResourceProvider provider;
    String uid;
    Timestamp created;

    public Account(Account account) {
        this.id = account.id;
        this.provider = account.provider;
        this.uid = account.uid;
        this.created = account.created;
    }
}
