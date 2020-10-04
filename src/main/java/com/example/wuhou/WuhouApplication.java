package com.example.wuhou;

import com.example.wuhou.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WuhouApplication {

    public static void main(String[] args) {
        SpringApplication.run(WuhouApplication.class, args);
//        Logger logger = LoggerFactory.getLogger(User.class);
//        logger.info("测试用户日志");

    }

}
