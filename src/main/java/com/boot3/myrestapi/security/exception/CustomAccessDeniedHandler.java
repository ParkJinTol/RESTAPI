package com.boot3.myrestapi.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

// 403
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // Set response content type to JSON
        response.setContentType("application/json;charset=UTF-8");
        // Set response code
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Create response content
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", HttpServletResponse.SC_FORBIDDEN);
            jsonObject.put("message", "요청된 리소스에 대한 접근 권한이 없습니다!");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Add content to the response
        log.error(accessDeniedException.getMessage(), accessDeniedException);
        response.getWriter().write(jsonObject.toString());

    }
}