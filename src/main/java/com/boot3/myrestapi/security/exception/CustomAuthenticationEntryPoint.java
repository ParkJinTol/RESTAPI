package com.boot3.myrestapi.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 401
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException authException)
            throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        //Set response status code 401
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create response content
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.put("message", "요청된 리소스에 대한 유효한 인증 자격 증명이 없습니다!");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        log.error(authException.getMessage(), authException);
        res.getWriter().write(jsonObject.toString());

    }
}