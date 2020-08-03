package com.starbun.bot.commands;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProfileCommand extends Command {
    @Override
    public void execute() {
        targetChannel.sendMessage(answer.build()).submit();
    }
}
