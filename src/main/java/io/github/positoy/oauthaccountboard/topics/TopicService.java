package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    TopicRepository topicRepository;

    public void postTopic(Topic topic) {
        logger.debug("");
        topicRepository.save(new TopicEntity(topic.getTitle(), topic.getContent(), topic.getView(), Integer.parseInt(topic.getAccount_id())));
    }

    public Topic getTopic(int id) {
        logger.debug("");
        Optional<TopicEntity> optionalTopicEntity = topicRepository.findById(id);
        Topic topic = null;
        if (optionalTopicEntity.isPresent()) {
            TopicEntity topicEntity = optionalTopicEntity.get();
            topic = new Topic(topicEntity.getId(), topicEntity.getTitle(), topicEntity.getContent(), topicEntity.getView(), topicEntity.getCreated(), String.valueOf(topicEntity.getAccount_id()));
        }
        return topic;
    }

    public List<TopicListItem> getTopics(int limit, int page, String keyword) {
        logger.debug("");
        if (page < 1) page = 1;
        int offset = DEFAULT_PAGE_SIZE * (page - 1);

        PageRequest pageRequest = PageRequest.of(page-1, limit, Sort.by(Sort.Direction.DESC, "created"));

        List<TopicEntity> topicEntities = topicRepository.findAll(pageRequest).getContent();
        List<TopicListItem> list = new ArrayList<>();
        for (TopicEntity entity : topicEntities)
            list.add(new TopicListItem(entity.getId(), entity.getTitle(), entity.getView(), entity.getCreated(), entity.getAccount_id()));

        return list;
    }

    public void updateTopic(int id, String title, String content) {
        logger.debug("");
        Optional<TopicEntity> optionalTopicEntity = topicRepository.findById(id);
        if (optionalTopicEntity.isPresent()) {
            TopicEntity topicEntity = optionalTopicEntity.get();
            topicEntity.title = title;
            topicEntity.content = content;
            topicRepository.save(topicEntity);
        }
    }

    public void updateTopicView(int id) {
        logger.debug("");
        Optional<TopicEntity> optionalTopicEntity = topicRepository.findById(id);
        if (optionalTopicEntity.isPresent()) {
            TopicEntity topicEntity = optionalTopicEntity.get();
            topicEntity.view = topicEntity.view + 1;
            topicRepository.save(topicEntity);
        }
    }

    public void deleteTopic(int id) {
        logger.debug("");
        topicRepository.deleteById(id);
    }

    public List<Page> getPages(int page) {
        ArrayList<Page> pages = new ArrayList<>();
        int lastPage = getLastPage();

        // firstIndex, lastIndex
        int firstIndex = ((page - 1) / 10) * 10 + 1;
        int lastIndex = (firstIndex + 9 <= lastPage) ? (firstIndex + 9) : lastPage;

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
        int count = (int)topicRepository.count();
        return (count / DEFAULT_PAGE_SIZE) + (count % DEFAULT_PAGE_SIZE == 0 ? 0 : 1);
    }
}
