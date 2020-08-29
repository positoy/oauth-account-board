package io.github.positoy.oauthaccountboard.models.entity;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AccountEntity {
    int id;
    String provider;
    String uid;
    Timestamp created;
}
