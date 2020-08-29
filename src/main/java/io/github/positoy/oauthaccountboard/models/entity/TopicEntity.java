package io.github.positoy.oauthaccountboard.models.entity;

import lombok.*;

import java.sql.Timestamp;

@Getter
public class TopicEntity {
    int id;
    String title;
    String content;
    int view;
    Timestamp created;
    int account_id;
}
