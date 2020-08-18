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
public class TopicListItem {
    int id;
    String title;
    Timestamp created;
    int author_id;
}
