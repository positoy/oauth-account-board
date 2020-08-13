package io.github.positoy.oauthaccountboard;

import io.github.positoy.oauthaccountboard.model.Topic;
import io.github.positoy.oauthaccountboard.model.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BoardService {
    final static Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    BoardRepository boardRepository;

    public void postTopic(Topic topic) {
        logger.debug("");
        boardRepository.add(topic);
    }

    public Topic getTopic(int id) {
        logger.debug("");
        return boardRepository.get(id);
    }

    public ArrayList<TopicListItem> getTopics() {
        logger.debug("");
        return boardRepository.getList();
    }

    public void updateTopic(int id, String title, String content) {
        logger.debug("");

        Topic topic = new Topic();
        topic.setId(id);
        topic.setTitle(title);
        topic.setContent(content);

        boardRepository.update(topic);
    }

    public void deleteTopic(int id) {
        logger.debug("");
        boardRepository.delete(id);
    }
}
