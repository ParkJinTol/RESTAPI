package com.boot3.myrestapi.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@Order(1)
@Slf4j
public class DatabaseRunner implements ApplicationRunner {
    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DataSource 구현객체는 = {} " , dataSource.getClass().getName());
        try (Connection connection = dataSource.getConnection()) {
            log.info(" DB URL = {}", connection.getMetaData().getURL());
            log.info(" DB UserName = {}", connection.getMetaData().getUserName());
        }
    }
}