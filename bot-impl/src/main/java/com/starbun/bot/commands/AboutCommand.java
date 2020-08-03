package com.starbun.bot.commands;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Команда возвращающая описание бота и его возможностей.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AboutCommand extends Command {

    @Override
    public void execute() {
        targetChannel.sendMessage(answer.build()).submit();
    }
}
