package io.github.positoy.oauthaccountboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BoardController {

    final static Logger logger = LoggerFactory.getLogger(BoardController.class);

    @GetMapping("/login")
    public String loginNaver(@RequestParam(defaultValue="") String code) {
        logger.info("code : " + code);
        return "redirect:/board";
    }

    @GetMapping("/board")
    @ResponseBody
    public String getBoard(@RequestParam(defaultValue="") String code) {
        return "Hello World!";
    }
}
