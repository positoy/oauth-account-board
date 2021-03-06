package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.models.AccountSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @GetMapping("/login/{provider}")
    public String loginKakao(HttpServletRequest request, @PathVariable String provider, @RequestParam(defaultValue="") String code, @RequestParam(defaultValue="") String state) {

        String requestParamLog = String.format("provider : %s, auth_code : %s, state : %s", provider, code, state);
        logger.info(requestParamLog);

        ResourceProvider resourceProvider = null;
        if (provider.equalsIgnoreCase("naver"))
            resourceProvider = ResourceProvider.NAVER;
        else if (provider.equalsIgnoreCase("kakao"))
            resourceProvider = ResourceProvider.KAKAO;

        // todo : use cookie instead of the session
        AccountSession accountSession = accountService.getSession(resourceProvider, code);
        if (accountSession != null) {
            logger.info("accountSession is not null");
            request.getSession().setAttribute("accountSession", accountSession);
        } else {
            logger.warn("accountSession is null");
        }

        return "redirect:/topics";
    }

}
