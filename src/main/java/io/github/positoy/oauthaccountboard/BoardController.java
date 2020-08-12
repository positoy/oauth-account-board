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

    @GetMapping("/login")
    public String loginNaver(
            HttpServletRequest request,
            @RequestParam(defaultValue="") String code,
            @RequestParam(defaultValue="") String state
    ) {
        logger.info("auth_code : " + code + ", state : " + state);
        Account account = loginService.login(ResourceProvider.NAVER, code);
        request.setAttribute("account", account);
        return "redirect:/board";
    }

    @GetMapping("/board")
    @ResponseBody
    public String getBoard(@RequestParam(defaultValue="") String code) {
        return "Hello World!";
    }
}
