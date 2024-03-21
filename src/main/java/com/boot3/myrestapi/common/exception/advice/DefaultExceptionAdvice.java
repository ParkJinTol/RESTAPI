package com.boot3.myrestapi.common.exception.advice;

import com.boot3.myrestapi.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DefaultExceptionAdvice {
private final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionAdvice.class);
@ExceptionHandler(BusinessException.class)
protected ResponseEntity<Object> handleException(BusinessException e) {
Map<String, Object> result = new HashMap<String, Object>();
result.put("message", "[안내] " + e.getMessage());
result.put("httpStatus", e.getHttpStatus().value());
return new ResponseEntity<>(result, e.getHttpStatus());
}
}