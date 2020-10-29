package com.example.wuhou;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@SpringBootTest
class WuhouApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() throws URISyntaxException {
//        File file = new File(".\\src\\main\\resources\\static\\webapp\\wordtemplate");
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(file.isDirectory());
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = classLoader.getResource("/static/webapp/PDF/sd2.pdf").;

//        ClassLoader classLoader = WuhouApplicationTests.class.getClassLoader();
//        URL url = classLoader.getResource("static/webapp/PDF/sd2.pdf");
//        String path = url.getPath();
//        File file = new File(url.getFile());
//        System.out.println(file.getPath());
//        URL url_WORD_TEMPLATE = WuhouApplication.class.getClassLoader().getResource(SOURCE_PATH + "/" + "wordtemplate" + "/" + "template.docx");
////        URI uri = new URI(url_WORD_TEMPLATE.toString());
//        String path = url_WORD_TEMPLATE.getPath();
//        File file = new File(path);
//        if (file.exists()){
//            System.out.println("文件存在");
//        }

    }
}
