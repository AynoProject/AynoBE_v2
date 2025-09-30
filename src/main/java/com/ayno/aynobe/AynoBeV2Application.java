package com.ayno.aynobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AynoBeV2Application {

    public static void main(String[] args) {
        SpringApplication.run(AynoBeV2Application.class, args);
    }

}
