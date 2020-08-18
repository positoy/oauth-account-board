package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.models.Account;
import io.github.positoy.oauthaccountboard.models.AccountSession;
import io.github.positoy.oauthaccountboard.oauth.naver.AccessTokenAPI;
import io.github.positoy.oauthaccountboard.oauth.Profile;
import io.github.positoy.oauthaccountboard.oauth.naver.ProfileAPI;
import io.github.positoy.oauthaccountboard.oauth.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    final static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    LoginRepository loginRepository;

    public AccountSession getNaverSession(String auth_code) {

        Token token = AccessTokenAPI.get(auth_code);
        logger.info("token : " + token.toString());
        Profile profile = ProfileAPI.get(token.access_token);
        logger.info("profile : " + profile.toString());

        if (!loginRepository.isAccountExist(ResourceProvider.NAVER, profile.getId()))
            if (!loginRepository.createAccount(ResourceProvider.NAVER, profile.getId()))
                return null;

        Account account = loginRepository.get(ResourceProvider.NAVER, profile.getId());
        return new AccountSession(account, token, profile);
    }

    public AccountSession getSession(ResourceProvider resourceProvider, String auth_code) {
        if (ResourceProvider.NAVER == resourceProvider) {
            return getNaverSession(auth_code);
        }

        return null;
    }

}
