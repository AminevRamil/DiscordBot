package com.starbun.bot.commands;

import com.starbun.bot.Bot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Команда задающая режим дебага в чате.
 */
@Data
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@EqualsAndHashCode(callSuper = true)
public class DebugCommand extends Command {

    Bot bot;
    private Boolean mode;

    @Autowired
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        targetChannel.sendMessage(answer.build()).submit();
        bot.setDebugMode(mode);
    }


}
