package io.github.positoy.oauthaccountboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class LoginRepository {
    final static Logger logger = LoggerFactory.getLogger(LoginRepository.class);

    @Value("${spring.datasource.driver-class-name}")
    String db_driver;
    @Value("${spring.datasource.url}")
    String db_url;
    @Value("${spring.datasource.username}")
    String db_username;
    @Value("${spring.datasource.password}")
    String db_password;

    boolean isReady = false;

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
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        isReady = true;
        return true;
    }

    public boolean isAccountExist(ResourceProvider resourceProvider, String id) {
        if (!isReady)
            readyRepository();

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
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return itemExist;
    }

    public int getAccountId(ResourceProvider resourceProvider, String id) {
        if (!isReady)
            readyRepository();

        int accountId = -1;
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
                accountId = resultSet.getInt("id");
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return accountId;
    }

    public boolean createAccount(ResourceProvider resourceProvider, String id) {
        if (!isReady)
            readyRepository();

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
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        return true;
    }
}
