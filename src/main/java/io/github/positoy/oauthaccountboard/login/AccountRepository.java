package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.ResourceProvider;
import io.github.positoy.oauthaccountboard.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AccountRepository {
    static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean create(ResourceProvider resourceProvider, String id) {
        try {
            String sql = "insert into account(provider, uid) values(?, ?)";
            jdbcTemplate.update(sql, new Object[]{resourceProvider.getServiceName(), id});
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return true;
    }

    public Account read(ResourceProvider resourceProvider, String uid) {
        Account account = null;
        try {
            String sql = "select * from account where provider=? and uid=?";
            account = jdbcTemplate.queryForObject(sql, new Object[]{resourceProvider.getServiceName(), uid}, new AccountMapper());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
        }

        return account;
    }

    public boolean exist(ResourceProvider resourceProvider, String id) {
        Account account = null;
        try {
            String sql = "select * from account where provider=? and uid=?";
            account = jdbcTemplate.queryForObject(sql, new Object[]{resourceProvider.getServiceName(), id}, new AccountMapper());
        } catch (Exception e) {
            e.printStackTrace();
            handleDBNotExist(e);
            return false;
        }

        return account != null;
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

    class AccountMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int i) throws SQLException {
            String provider = rs.getString("provider");

            ResourceProvider resourceProvider = null;
            if (provider == "naver") resourceProvider = ResourceProvider.NAVER;
            else if (provider.equalsIgnoreCase("kakao")) resourceProvider = ResourceProvider.KAKAO;
            else if (provider.equalsIgnoreCase("google")) resourceProvider = ResourceProvider.GOOGLE;
            else resourceProvider = ResourceProvider.FACEBOOOK;

            return new Account(rs.getInt("id"), resourceProvider, rs.getString("uid"), rs.getTimestamp("created"));
        }
    }
}
