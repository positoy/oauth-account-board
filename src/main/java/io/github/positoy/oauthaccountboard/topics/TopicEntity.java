package io.github.positoy.oauthaccountboard.topics;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "topic")
public class TopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 64)
    String title;

    @Column
    String content;

    @Column
    Integer view;

    @Column
    Timestamp created;

    @Column
    Integer account_id;

    @Builder
    public TopicEntity(String title, String content, Integer view, Integer account_id) {
        this.title = title;
        this.content = content;
        this.view = view;
        this.created = new Timestamp(System.currentTimeMillis());
        this.account_id = account_id;
    }
}
