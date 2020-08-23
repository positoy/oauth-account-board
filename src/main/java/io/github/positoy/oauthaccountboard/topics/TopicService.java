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

    public final static int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    TopicRepository topicRepository;

    public void postTopic(Topic topic) {
        logger.debug("");
        topicRepository.create(topic);
    }

    public Topic getTopic(int id) {
        logger.debug("");
        return topicRepository.read(id);
    }

    public ArrayList<TopicListItem> getTopics(int limit, int page) {
        logger.debug("");
        if (page < 1) page = 1;
        int offset = DEFAULT_PAGE_SIZE * (page - 1);
        return topicRepository.read(limit, offset);
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

    public ArrayList<Page> getPages(int page) {
        ArrayList<Page> pages = new ArrayList<>();
        int lastPage = getLastPage();

        // firstIndex, lastIndex
        int firstIndex = (int)((page - 1) / 10) * 10 + 1;
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
        int count = topicRepository.count();
        return (count / DEFAULT_PAGE_SIZE) + (count % DEFAULT_PAGE_SIZE == 0 ? 0 : 1);
    }
}
