package io.github.positoy.oauthaccountboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BoardController {

    final static Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public String loginNaver(@RequestParam(defaultValue="") String code) {
        logger.info("code : " + code);
        int accountId = loginService.getAccountId(ResourceServer.NAVER, code);
        logger.info("accountId : " + accountId);
        return "redirect:/board";
    }

    @GetMapping("/board")
    @ResponseBody
    public String getBoard(@RequestParam(defaultValue="") String code) {
        return "Hello World!";
    }
}
