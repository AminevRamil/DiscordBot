package com.starbun.bot;

import lombok.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Data
@Component
public class BotStarter {

    private JDA jda;

    @Value("${bot.token}")
    String token;

    public void startBot() throws LoginException {
        this.jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("раБОТу"))
                .build();
    }

    public void registerListeners(Object... eventListeners) {
        this.jda.addEventListener(eventListeners);
    }
}
