package io.github.positoy.oauthaccountboard.login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    int countByProviderAndUid(String provider, String uid);
    AccountEntity findByProviderAndUid(String provider, String uid);
}
