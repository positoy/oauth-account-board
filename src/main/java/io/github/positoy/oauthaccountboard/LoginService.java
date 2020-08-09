package io.github.positoy.oauthaccountboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    final static Logger logger = LoggerFactory.getLogger(LoginService.class);

    public int getAccountId(ResourceServer resourceServer, String authcode) {
        logger.info("serviceName : " + resourceServer.getServiceName() + ", authcode : " + authcode);

        return 0;
    }
}
