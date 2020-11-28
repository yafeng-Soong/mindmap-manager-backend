package com.syf.papermanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.syf.papermanager.mapper")
public class PaperManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperManagerApplication.class, args);
    }

}
