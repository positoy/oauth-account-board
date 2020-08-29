package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.models.Account;
import io.github.positoy.oauthaccountboard.models.AccountSession;
import io.github.positoy.oauthaccountboard.models.entity.AccountEntity;
import io.github.positoy.oauthaccountboard.oauth.Profile;
import io.github.positoy.oauthaccountboard.oauth.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountMapper accountMapper;

    public AccountSession getSession(ResourceProvider resourceProvider, String authcode) {

        Token token = null;
        Profile profile = null;

        if (ResourceProvider.NAVER == resourceProvider) {
            token = io.github.positoy.oauthaccountboard.oauth.naver.AccessTokenAPI.get(authcode);
            profile = io.github.positoy.oauthaccountboard.oauth.naver.ProfileAPI.get(token.access_token);
        } else if (ResourceProvider.KAKAO == resourceProvider) {
            token = io.github.positoy.oauthaccountboard.oauth.kakao.AccessTokenAPI.get(authcode);
            profile = io.github.positoy.oauthaccountboard.oauth.kakao.ProfileAPI.get(token.access_token);
        }

        String resultLog = String.format("token : %s, profile : %s",
                token == null ? "null" : token.toString(),
                profile == null ? "null" : profile.toString()
        );
        logger.info(resultLog);

        if (accountMapper.getAccount(resourceProvider.getServiceName(), profile.getId()) == null &&
            accountMapper.insertAccount(resourceProvider.getServiceName(), profile.getId()) == 0)
            return null;

        AccountEntity accountEntity = accountMapper.getAccount(resourceProvider.getServiceName(), profile.getId());
        Account account = new Account(accountEntity.getId(), resourceProvider, accountEntity.getUid(), accountEntity.getCreated());
        return new AccountSession(account, token, profile);
    }

}
