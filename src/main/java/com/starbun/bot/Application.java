package com.starbun.bot;


import com.starbun.bot.util.BotStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

import javax.security.auth.login.LoginException;

@EnableConfigurationProperties
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class Application implements ApplicationRunner {

    private Bot bot;
    private BotStarter botStarter;
    public static ConfigurableApplicationContext context;


    @Autowired
    public Application(Bot bot, BotStarter botStarter) {
        this.bot = bot;
        this.botStarter = botStarter;
    }

    public static void main(String[] args) throws LoginException {
        context = SpringApplication.run(Application.class);

        //ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigure.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        botStarter.startBot();
        botStarter.registerListeners(bot);
    }
}
