package io.github.positoy.oauthaccountboard.login;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 64)
    String provider;

    @Column(length = 128)
    String uid;

    @Column
    Timestamp created;

    @Builder
    public AccountEntity(String provider, String uid) {
        this.provider = provider;
        this.uid = uid;
        this.created = new Timestamp(System.currentTimeMillis());
    }
}
