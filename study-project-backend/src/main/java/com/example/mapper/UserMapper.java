package com.example.mapper;

import com.example.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from db_account where username = #{username} or email = #{password}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into db_account (username, email, password) values (#{username}, #{email}, #{password})")
    int createAccount(String username, String email, String password);

}
