package io.github.positoy.oauthaccountboard;

import io.github.positoy.oauthaccountboard.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BoardController {

    final static Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    LoginService loginService;

    @GetMapping("/login/naver")
    public String loginNaver(
            HttpServletRequest request,
            @RequestParam(defaultValue="") String code,
            @RequestParam(defaultValue="") String state
    ) {
        logger.info("auth_code : " + code + ", state : " + state);
        Account account = loginService.login(ResourceProvider.NAVER, code);
        // todo : use cookie instead of the session
        request.getSession().setAttribute("account", account);
        return "redirect:/board";
    }

    @GetMapping("/board")
    @ResponseBody
    public String getBoard(
            HttpServletRequest request,
            @RequestParam(defaultValue="") String code) {
        Account account = (Account)request.getSession().getAttribute("account");

        String ret = "<h1>Hello World</h1>" +
                "<p>" + ((Account)request.getSession().getAttribute("account")).toString() + "</p>";
        return ret;
    }
}
