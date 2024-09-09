package com.example.mapper;

import com.example.entity.auth.Account;
import com.example.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from db_account where username = #{username} or email = #{password}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from db_account where username = #{username} or email = #{password}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into db_account (username, email, password) values (#{username}, #{email}, #{password})")
    int createAccount(String username, String email, String password);

    @Update("update db_account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String email, String password);

}
