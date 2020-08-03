package com.starbun.bot;


import com.starbun.bot.config.ApplicationConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    }
}
