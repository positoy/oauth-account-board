package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AccountRepository {
    final static Logger logger = LoggerFactory.getLogger(AccountRepository.class);

    String db_driver;
    String db_url;
    String db_username;
    String db_password;

    public AccountRepository(
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

    public boolean create(ResourceProvider resourceProvider, String id) {
        String sql = "insert into account(provider, uid, created) values(?, ?, now())";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, resourceProvider.getServiceName());
            preparedStatement.setString(2, id);

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

    public Account read(ResourceProvider resourceProvider, String uid) {
        Account account = null;
        String sql = "select * from account where provider=? and uid=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, resourceProvider.getServiceName());
            preparedStatement.setString(2, uid);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                account = new Account(resultSet.getInt("id"), resourceProvider, uid, resultSet.getTimestamp("created"));
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

        return account;
    }

    public boolean exist(ResourceProvider resourceProvider, String id) {
        boolean itemExist = false;
        String sql = "select * from account where provider=? and uid=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(db_driver);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, resourceProvider.getServiceName());
            preparedStatement.setString(2, id);

            logger.info(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                itemExist = true;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return itemExist;
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

    private void handleDBNotExist(Exception e) {
        if (e.getMessage().contains("doesn't exist"))
            readyRepository();
    }
}
