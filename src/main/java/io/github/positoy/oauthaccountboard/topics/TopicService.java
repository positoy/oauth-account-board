package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TopicService {
    final static Logger logger = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    TopicRepository topicRepository;

    public void postTopic(Topic topic) {
        logger.debug("");
        topicRepository.add(topic);
    }

    public Topic getTopic(int id) {
        logger.debug("");
        return topicRepository.get(id);
    }

    public ArrayList<TopicListItem> getTopics() {
        logger.debug("");
        return topicRepository.getList();
    }

    public void updateTopic(int id, String title, String content) {
        logger.debug("");

        Topic topic = new Topic();
        topic.setId(id);
        topic.setTitle(title);
        topic.setContent(content);

        topicRepository.update(topic);
    }

    public void deleteTopic(int id) {
        logger.debug("");
        topicRepository.delete(id);
    }
}
