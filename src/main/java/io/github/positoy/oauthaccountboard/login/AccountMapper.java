package io.github.positoy.oauthaccountboard.login;

import io.github.positoy.oauthaccountboard.models.entity.AccountEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
    @Insert("insert into account(provider, uid) values(#{provider}, #{id})")
    int insertAccount(@Param("provider") String provider, @Param("id") String id);

    @Select("select * from account where provider=#{provider} and uid=#{id}")
    AccountEntity getAccount(@Param("provider") String provider, @Param("id") String id);
}