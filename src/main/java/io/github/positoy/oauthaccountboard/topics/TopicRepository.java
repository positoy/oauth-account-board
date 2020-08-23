package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class TopicRepository {
    final static Logger logger = LoggerFactory.getLogger(TopicRepository.class);

    String db_driver;
    String db_url;
    String db_username;
    String db_password;

    public TopicRepository(
            @Value("${spring.datasource.driver-class-name}") String db_driver,
            @Value("${spring.datasource.url}") String db_url,
            @Value("${spring.datasource.username}") String db_username,
            @Value("${spring.datasource.password}") String db_password
    ) {
        this.db_driver = db_driver;
        this.db_url = db_url;
        this.db_username = db_username;
        this.db_password = db_password;
        readyRepository();
    }

    public boolean add(Topic topic) {
        if (topic == null)
            return false;

        String sql = "insert into topic(title, content, created, account_id) values(?,?,now(),?)";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, topic.getTitle());
            preparedStatement.setString(2, topic.getContent());
            preparedStatement.setString(3, topic.getAccount_id());

            logger.info(sql);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return true;
    }

    public Topic get(int id) {
        String sql = "select * from topic where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Topic returnTopic = null;
        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                final int topic_id = resultSet.getInt("id");
                final String topic_title = resultSet.getString("title");
                final String topic_content = resultSet.getString("content");
                final Timestamp topic_created = resultSet.getTimestamp("created");
                final String topic_account_id = resultSet.getString("account_id");

                returnTopic = new Topic(topic_id, topic_title, topic_content, topic_created, topic_account_id);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return returnTopic;
    }

    public ArrayList<TopicListItem> getList() {
        String sql = "select * from topic";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<TopicListItem> list = new ArrayList<>();
        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                final int topic_id = resultSet.getInt("id");
                final String topic_title = resultSet.getString("title");
                final Timestamp topic_created = resultSet.getTimestamp("created");
                final int topic_author_id = resultSet.getInt("account_id");

                list.add(new TopicListItem(topic_id, topic_title, topic_created, topic_author_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            list = null;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public boolean update(Topic topic) {
        if (topic == null)
            return false;

        String sql = "update topic set title=?,content=? where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, topic.getTitle());
            preparedStatement.setString(2, topic.getContent());
            preparedStatement.setInt(3, topic.getId());

            logger.info(sql);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return true;
    }

    public boolean delete(int id) {
        if (id <= 0)
            return false;

        String sql = "delete from topic where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            logger.info(sql);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return true;
    }

    private void handleDBNotExist(Exception e) {
        if (e.getMessage().contains("doesn't exist"))
            readyRepository();
    }

    boolean readyRepository() {

        Connection conn = null;
        Statement statement = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            statement = conn.createStatement();

            // Verify Database
            statement.execute("create database if not exists oauthboard");
            statement.execute("use oauthboard");

            // Verify Table Topic
            statement.execute("create table if not exists topic(" +
                    "id int not null primary key auto_increment," +
                    "title varchar(64) not null," +
                    "content text not null," +
                    "created datetime not null," +
                    "account_id int)");

            // Verify Account Topic
            statement.execute("create table if not exists account(" +
                    "id int not null primary key auto_increment," +
                    "provider varchar(64) not null," +
                    "uid varchar(128) not null," +
                    "created datetime not null)");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("failed to ready DB");
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return true;
    }
}
