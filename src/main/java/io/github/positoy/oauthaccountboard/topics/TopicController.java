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

    static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    static final String TEMPLATE_POST = "post";
    static final String TEMPLATE_TOPIC = "topic";
    static final String TEMPLATE_TOPICS = "topics";
    static final String TEMPLATE_PUT = "put";

    static final String REDIRECT_TOPICS = "redirect:/topics";

    static final String MODEL_PARAM_TOPIC = "topic";
    static final String MODEL_PARAM_TOPICS = "topics";
    static final String MODEL_PARAM_PAGES = "pages";

    @Autowired
    TopicService topicService;

    //////////////////////////
    ///// CREATE
    //////////////////////////
    @GetMapping("/topics/post")
    public String getTopicPost() {
        return TEMPLATE_POST;
    }

    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postTopic(HttpServletRequest request, @RequestBody MultiValueMap<String,String> paramMap) {
        Topic topic = null;
        if (paramMap != null) {
            topic = new Topic();
            topic.setTitle(paramMap.getFirst("title"));
            topic.setContent(paramMap.getFirst("content"));

            AccountSession accountSession  = (AccountSession)request.getSession().getAttribute("accountSession");
            topic.setAccount_id(accountSession == null ? "-1" : accountSession.getUid());
        }

        topicService.postTopic(topic);
        return REDIRECT_TOPICS;
    }

    //////////////////////////
    ///// READ
    //////////////////////////
    @GetMapping("/topics/{id}")
    public String getTopic(@PathVariable int id, Model model) {
        Topic topic = topicService.getTopic(id);
        topic.setView(topic.getView() + 1);
        model.addAttribute(MODEL_PARAM_TOPIC, topic);

        topicService.updateTopicView(id);
        return TEMPLATE_TOPIC;
    }

    @GetMapping("/topics")
    public String getTopics(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "1") int page, Model model) {
        String requestParamLog = String.format("limit : %d, page : %d", limit, page);
        logger.info(requestParamLog);
        List<TopicListItem> topics = topicService.getTopics(limit, page);
        model.addAttribute(MODEL_PARAM_TOPICS, topics);
        model.addAttribute(MODEL_PARAM_PAGES, topicService.getPages(page));
        return TEMPLATE_TOPICS;
    }

    //////////////////////////
    ///// UPDATE
    //////////////////////////
    @GetMapping("/topics/{id}/put")
    public String getTopicPut(@PathVariable int id, Model model) {
        Topic topic = topicService.getTopic(id);
        model.addAttribute(MODEL_PARAM_TOPIC, topic);
        return TEMPLATE_PUT;
    }

    @PostMapping("/topics/{id}")
    public String putTopic(@PathVariable int id, Topic topic) {
        topicService.updateTopic(id, topic.getTitle(), topic.getContent());
        return REDIRECT_TOPICS;
    }

    //////////////////////////
    ///// DELETE
    //////////////////////////
    @GetMapping("/topics/{id}/delete")
    public String deleteTopic(@PathVariable int id) {
        topicService.deleteTopic(id);
        return REDIRECT_TOPICS;
    }
}
