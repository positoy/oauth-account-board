package io.github.positoy.oauthaccountboard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    int id;
    String title;
    String content;
    Timestamp created;
    String account_id;
}
