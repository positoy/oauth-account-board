package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class TopicRepository {
    static final Logger logger = LoggerFactory.getLogger(TopicRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean create(Topic topic) {
        if (topic == null)
            return false;

        try {
            String sql = "insert into topic(title, content, account_id) values(?,?,?)";
            jdbcTemplate.update(sql, topic.getTitle(), topic.getContent(), topic.getAccount_id());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return true;
    }

    public Topic read(int id) {
        Topic topic = null;
        try {
            String sql = "select * from topic where id=?";
            topic = jdbcTemplate.queryForObject(sql, new Object[]{id}, new TopicMapper());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
        }

        return topic;
    }

    public List<TopicListItem> read(int limit, int offset, String keyword) {
        List<TopicListItem> list = null;
        try {
            String sql = keyword.isEmpty() ?
                    "select * from topic order by created desc limit ? offset ?" :
                    "select * from topic where title like ? or content like ? order by created desc limit ? offset ?";

            Object[] paramList = keyword.isEmpty() ?
                    new Object[]{limit, offset} :
                    new Object[]{String.format("%%%s%%", keyword), String.format("%%%s%%", keyword), limit, offset};

            list = jdbcTemplate.query(sql, paramList, new TopicListItemMapper());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
        }

        return list;
    }

    public boolean update(Topic topic) {
        if (topic == null)
            return false;

        try {
            String sql = "update topic set title=?,content=? where id=?";
            jdbcTemplate.update(sql, topic.getTitle(), topic.getContent(), topic.getId());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return true;
    }

    public boolean updateView(int id) {
        try {
            String sql = "update topic set view=view+1 where id=?";
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return true;
    }

    public boolean delete(int id) {
        if (id <= 0)
            return false;

        try {
            String sql = "delete from topic where id=?";
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return true;
    }

    public int count() {
        int count = -1;
        try {
            String sql = "select count(*) from topic";
            count = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    boolean readyRepository() {
        try {
            // Verify Database
            jdbcTemplate.update("create database if not exists oauthboard");
            jdbcTemplate.update("use oauthboard");

            // Verify Table Topic
            jdbcTemplate.update("create table if not exists topic(" +
                    "id int not null primary key auto_increment," +
                    "title varchar(64) not null," +
                    "content text not null," +
                    "view int default 0," +
                    "created datetime default now()," +
                    "account_id int default -1)");

            // Verify Account Topic
            jdbcTemplate.update("create table if not exists account(" +
                    "id int not null primary key auto_increment," +
                    "provider varchar(64) not null," +
                    "uid varchar(128) not null," +
                    "created datetime default now())");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("failed to ready DB");
            return false;
        }

        return true;
    }

    private void handleDBNotExist(Exception e) {
        if (e.getMessage().contains("doesn't exist"))
            readyRepository();
    }

    class TopicMapper implements RowMapper<Topic> {
        @Override
        public Topic mapRow(ResultSet rs, int i) throws SQLException {
            return new Topic(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("view"),
                    rs.getTimestamp("created"),
                    Integer.toString(rs.getInt("account_id"))
            );
        }
    }

    class TopicListItemMapper implements RowMapper<TopicListItem> {
        @Override
        public TopicListItem mapRow(ResultSet rs, int i) throws SQLException {
            return new TopicListItem(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("view"),
                    rs.getTimestamp("created"),
                    rs.getInt("account_id")
            );
        }
    }
}
