package dev.noteforge.knowhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnowhubApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowhubApplication.class, args);
    }
}
