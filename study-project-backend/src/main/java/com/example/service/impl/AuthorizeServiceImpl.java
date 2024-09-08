package com.example.service.impl;

import com.example.entity.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper mapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误！");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    /*
          1. 先生成对应的验证码
          2. 把邮箱和对应的验证码直接放到Redis里面（过期时间3分钟，如果此时重新要求发邮件，
          那么只要剩余时间低于2分钟，就可以重新发送一次，重复此流程）
          3. 发送验证码到指定邮箱
          4. 如果发送失败，把Redis里面的刚刚插入的删除
          5. 用户在注册时，再从Redis里面去除对应键值对，然后看验证码是否一致
         */
    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {

        String key = "email: " + sessionId + ":" + email + ":" + hasAccount;
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return "访问频繁，请稍后再试";
        }
        Account account = mapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null)
            return "找不到此邮箱地址的用户";
        if (!hasAccount && account != null)
            return "此邮箱已被注册";
        Random random = new Random();
        int code = random.nextInt(899999)+100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证码");
        message.setText("您的验证码是: "+code);

        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "获取验证码失败，请检查邮箱地址是否正确";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email: " + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码已过期，请重新请求";
            if (s.equals(code)) {
                Account account = mapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册，请更换用户名";
                template.delete(key);
                password = encoder.encode(password);
                if (mapper.createAccount(username, email, password) > 0) return null;
                else return "内部错误，请联系管理员";
            } else return "验证码错误，请检查后再提交";
        } else return "请先请求一封验证码邮件";
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email: " + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码已过期，请重新请求";
            if (s.equals(code)) {
                template.delete(key);
                return null;
            } else return "验证码错误，请检查后再提交";
        } else return "请先请求一封验证码邮件";
    }

    @Override
    public Boolean resetPassword(String email, String password) {
        password = encoder.encode(password);
        return mapper.resetPasswordByEmail(email, password) > 0;
    }
}
