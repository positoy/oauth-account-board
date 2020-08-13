package io.github.positoy.oauthaccountboard;

import io.github.positoy.oauthaccountboard.model.Account;
import io.github.positoy.oauthaccountboard.model.Topic;
import io.github.positoy.oauthaccountboard.model.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class BoardController {

    final static Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    BoardService boardService;

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
        return "redirect:/topics";
    }

    @PostMapping("/topics")
    public String postTopic(HttpServletRequest request, @RequestBody Topic topic) {
        Account account = (Account)request.getSession().getAttribute("account");
        logger.info(account.toString());
        topic.setAuthor_id(account.getId());
        boardService.postTopic(topic);
        return "redirect:/topics";
    }

    @GetMapping("/topics/{id}")
    public String getTopic(@PathVariable int id, Model model) {
        Topic topic = boardService.getTopic(id);
        model.addAttribute("topic", topic);
        return "redirect:/content.html";
    }

    @GetMapping("/topics")
    public String getBoard(Model model) {
        ArrayList<TopicListItem> list = boardService.getTopics();
        model.addAttribute("list", list);
        return "board";
    }

    @GetMapping("/topics/post")
    public String postTopic(HttpServletRequest request) {
        return "/post.html";
    }

    @PostMapping("/topics/{id}")
    public String postTopic(@PathVariable int id, Topic topic) {
        boardService.updateTopic(id, topic.getTitle(), topic.getContent());
        return "redirect:/topics";
    }

    @GetMapping("/topics/{id}/delete")
    public String postTopic(@PathVariable int id) {
        boardService.deleteTopic(id);
        return "redirect:/topics";
    }
}
