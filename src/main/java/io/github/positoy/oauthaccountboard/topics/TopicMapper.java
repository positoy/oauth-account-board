package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.entity.TopicEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TopicMapper {

    @Insert("insert into topic(title, content, account_id) values(#{title},#{content},#{account_id})")
    int insertTopic(@Param("title") String title, @Param("content") String content, @Param("account_id") int account_id);

    @Select("select * from topic where id=#{id}")
    TopicEntity getTopic(@Param("id") int id);

    @Select("select * from topic order by created desc limit #{limit} offset #{offset}")
    List<TopicEntity> getTopicPage(@Param("limit") int limit, @Param("offset") int offset);

    @Select("select * from topic where title like #{keyword} or content like #{keyword} order by created desc limit #{limit} offset #{offset}")
    List<TopicEntity> getTopicPageWithKeyword(@Param("limit") int limit, @Param("offset") int offset, @Param("keyword") String keyword);

    @Update("update topic set title=#{title},content=#{content} where id=#{id}")
    int updateTopic(@Param("title") String title, @Param("content") String content, @Param("id") int id);

    @Update("update topic set view=view+1 where id=#{id}")
    int updateTopicView(@Param("id") int id);

    @Delete("delete from topic where id=#{id}")
    int deleteTopic(@Param("id") int id);

    @Select("select count(*) from topic")
    int getTotalTopicCount();
}