package edu.hufe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FreeMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeMusicApplication.class, args);
    }

}
