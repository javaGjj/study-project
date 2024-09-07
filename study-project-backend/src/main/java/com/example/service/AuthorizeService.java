package com.example.service;

import com.example.entity.RestBean;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    String sendValidateEmail(String email, String sessionId);
    String validateAndRegister(String username, String password, String email, String code, String sessionId);
}
