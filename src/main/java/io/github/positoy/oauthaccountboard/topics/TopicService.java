package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import io.github.positoy.oauthaccountboard.models.entity.TopicEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {
    static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    TopicMapper topicMapper;

    public void postTopic(Topic topic) {
        logger.debug("");
        topicMapper.insertTopic(topic.getTitle(), topic.getContent(), Integer.parseInt(topic.getAccount_id()));
    }

    public Topic getTopic(int id) {
        logger.debug("");
        TopicEntity topicEntity = topicMapper.getTopic(id);
        return new Topic(topicEntity.getId(), topicEntity.getTitle(), topicEntity.getContent(), topicEntity.getView(), topicEntity.getCreated(), String.valueOf(topicEntity.getAccount_id()));
    }

    public List<TopicListItem> getTopics(int limit, int page, String keyword) {
        logger.debug("");
        if (page < 1) page = 1;
        int offset = DEFAULT_PAGE_SIZE * (page - 1);

        List<TopicEntity> entities = keyword.isEmpty() ?
                topicMapper.getTopicPage(limit, offset) :
                topicMapper.getTopicPageWithKeyword(limit, offset, "%" + keyword + "%");

        List<TopicListItem> list = new ArrayList<>();
        for (TopicEntity entity : entities)
            list.add(new TopicListItem(entity.getId(), entity.getTitle(), entity.getView(), entity.getCreated(), entity.getAccount_id()));

        return list;
    }

    public void updateTopic(int id, String title, String content) {
        logger.debug("");
        topicMapper.updateTopic(title, content, id);
    }

    public void updateTopicView(int id) {
        logger.debug("");
        topicMapper.updateTopicView(id);
    }

    public void deleteTopic(int id) {
        logger.debug("");
        topicMapper.deleteTopic(id);
    }

    public List<Page> getPages(int page) {
        ArrayList<Page> pages = new ArrayList<>();
        int lastPage = getLastPage();

        // firstIndex, lastIndex
        int firstIndex = ((page - 1) / 10) * 10 + 1;
        int lastIndex = Math.min(firstIndex + 9, lastPage);

        // previous
        boolean previousExist = (firstIndex != 1);
        if (previousExist) {
            String previousUrl = String.format("/topics?page=%d", firstIndex - 1);
            pages.add(new Page("Previous", previousUrl));
        }

        // indexes
        for (int i=firstIndex; i<=lastIndex; i++)
            pages.add(new Page(Integer.toString(i), "/topics?page="+i));

        // next
        boolean nextExist = (lastIndex != lastPage);
        if (nextExist) {
            String nextUrl = String.format("/topics?page=%d", lastIndex + 1);
            pages.add(new Page("Next", nextUrl));
        }

        return pages;
    }

    private int getLastPage() {
        int count = topicMapper.getTotalTopicCount();
        return (count / DEFAULT_PAGE_SIZE) + (count % DEFAULT_PAGE_SIZE == 0 ? 0 : 1);
    }
}
