package io.github.positoy.oauthaccountboard;

import io.github.positoy.oauthaccountboard.model.Account;
import io.github.positoy.oauthaccountboard.naver.AccessTokenAPI;
import io.github.positoy.oauthaccountboard.naver.Profile;
import io.github.positoy.oauthaccountboard.naver.ProfileAPI;
import io.github.positoy.oauthaccountboard.naver.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    final static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    LoginRepository loginRepository;

    public Account loginNaver(String auth_code) {

        Token token = AccessTokenAPI.get(auth_code);
        logger.info("token : " + token.toString());
        Profile profile = ProfileAPI.get(token.access_token);
        logger.info("profile : " + profile.toString());

        if (!loginRepository.isAccountExist(ResourceProvider.NAVER, profile.getName()))
            if (!loginRepository.createAccount(ResourceProvider.NAVER, profile.getName()))
                return null;

        int accountId = loginRepository.getAccountId(ResourceProvider.NAVER, profile.getName());

        return new Account(accountId, ResourceProvider.NAVER, token, profile);
    }

    public Account login(ResourceProvider resourceProvider, String auth_code) {
        if (ResourceProvider.NAVER == resourceProvider) {
            return loginNaver(auth_code);
        }

        return null;
    }

}
