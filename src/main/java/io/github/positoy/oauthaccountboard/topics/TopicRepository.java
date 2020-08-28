package io.github.positoy.oauthaccountboard.topics;

import io.github.positoy.oauthaccountboard.models.Topic;
import io.github.positoy.oauthaccountboard.models.TopicListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TopicRepository {
    static final Logger logger = LoggerFactory.getLogger(TopicRepository.class);

    String dbDRIVER;
    String dbURL;
    String dbUSERNAME;
    String dbPASSWORD;

    public TopicRepository(
            @Value("${spring.datasource.driver-class-name}") String dbDRIVER,
            @Value("${spring.datasource.url}") String dbURL,
            @Value("${spring.datasource.username}") String dbUSERNAME,
            @Value("${spring.datasource.password}") String dbPASSWORD
    ) {
        this.dbDRIVER = dbDRIVER;
        this.dbURL = dbURL;
        this.dbUSERNAME = dbUSERNAME;
        this.dbPASSWORD = dbPASSWORD;
        readyRepository();
    }

    public boolean create(Topic topic) {
        if (topic == null)
            return false;

        String sql = "insert into topic(title, content, account_id) values(?,?,?)";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
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

    public Topic read(int id) {
        String sql = "select * from topic where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Topic returnTopic = null;
        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                final int topic_id = resultSet.getInt("id");
                final String topic_title = resultSet.getString("title");
                final String topic_content = resultSet.getString("content");
                final int topic_view = resultSet.getInt("view");
                final Timestamp topic_created = resultSet.getTimestamp("created");
                final String topic_account_id = resultSet.getString("account_id");

                returnTopic = new Topic(topic_id, topic_title, topic_content, topic_view, topic_created, topic_account_id);
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

    public List<TopicListItem> read(int limit, int offset) {
        String sql = "select * from topic order by created desc limit ? offset ?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<TopicListItem> list = new ArrayList<>();
        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                final int topic_id = resultSet.getInt("id");
                final String topic_title = resultSet.getString("title");
                final int topic_view = resultSet.getInt("view");
                final Timestamp topic_created = resultSet.getTimestamp("created");
                final int topic_account_id = resultSet.getInt("account_id");

                list.add(new TopicListItem(topic_id, topic_title, topic_view, topic_created, topic_account_id));
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

        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
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
        }

        return true;
    }

    public boolean updateView(int id) {
        String sql = "update topic set view=view+1 where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
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
        }

        return true;
    }

    public boolean delete(int id) {
        if (id <= 0)
            return false;

        String sql = "delete from topic where id=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
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
        }

        return true;
    }

    public int count() {
        String sql = "select count(*) from topic";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = -1;
        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
            preparedStatement = conn.prepareStatement(sql);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                count = resultSet.getInt(1);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return count;
    }

    boolean readyRepository() {

        Connection conn = null;
        Statement statement = null;

        try {
            Class.forName(dbDRIVER);
            conn = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);
            statement = conn.createStatement();

            // Verify Database
            statement.execute("create database if not exists oauthboard");
            statement.execute("use oauthboard");

            // Verify Table Topic
            statement.execute("create table if not exists topic(" +
                    "id int not null primary key auto_increment," +
                    "title varchar(64) not null," +
                    "content text not null," +
                    "view int default 0," +
                    "created datetime default now()," +
                    "account_id int default -1)");

            // Verify Account Topic
            statement.execute("create table if not exists account(" +
                    "id int not null primary key auto_increment," +
                    "provider varchar(64) not null," +
                    "uid varchar(128) not null," +
                    "created datetime default now())");

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

    private void handleDBNotExist(Exception e) {
        if (e.getMessage().contains("doesn't exist"))
            readyRepository();
    }
}
