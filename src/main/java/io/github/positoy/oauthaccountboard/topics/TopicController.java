package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.AccountSession;
import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TopicController {

    final static Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    TopicService topicService;

    //////////////////////////
    ///// CREATE
    //////////////////////////
    @GetMapping("/topics/post")
    public String getTopicPost() {
        return "post";
    }

    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postTopic(HttpServletRequest request, @RequestBody MultiValueMap<String,String> paramMap) {
        Topic topic = null;
        if (paramMap != null) {
            topic = new Topic();
            topic.setTitle(paramMap.getFirst("title"));
            topic.setContent(paramMap.getFirst("content"));

            AccountSession accountSession  = (AccountSession)request.getSession().getAttribute("accountSession");
            logger.info(String.format("AccountSession : %s", accountSession == null ? "null" : accountSession.toString()));
            topic.setAccount_id(accountSession == null ? "-1" : accountSession.getUid());
        }

        topicService.postTopic(topic);
        return "redirect:/topics";
    }

    //////////////////////////
    ///// READ
    //////////////////////////
    @GetMapping("/topics/{id}")
    public String getTopic(@PathVariable int id, Model model) {
        Topic topic = topicService.getTopic(id);
        model.addAttribute("topic", topic);
        return "topic";
    }

    @GetMapping("/topics")
    public String getTopics(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "1") int page, Model model) {
        logger.info(String.format("limit:%d, page:%d", limit, page));
        ArrayList<TopicListItem> topics = topicService.getTopics(limit, page);
        model.addAttribute("topics", topics);
        model.addAttribute("pages", topicService.getPages(page));
        logger.info(topicService.getPages(page).toString());
        return "topics";
    }

    //////////////////////////
    ///// UPDATE
    //////////////////////////
    @GetMapping("/topics/{id}/put")
    public String getTopicPut(@PathVariable int id, Model model) {
        Topic topic = topicService.getTopic(id);
        model.addAttribute("topic", topic);
        return "put";
    }

    @PostMapping("/topics/{id}")
    public String putTopic(@PathVariable int id, Topic topic) {
        topicService.updateTopic(id, topic.getTitle(), topic.getContent());
        return "redirect:/topics";
    }

    //////////////////////////
    ///// DELETE
    //////////////////////////
    @GetMapping("/topics/{id}/delete")
    public String deleteTopic(@PathVariable int id) {
        topicService.deleteTopic(id);
        return "redirect:/topics";
    }
}
