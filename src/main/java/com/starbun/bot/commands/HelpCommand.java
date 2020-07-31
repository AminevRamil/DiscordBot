package com.starbun.bot.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Команда выдающая справку о командах бота
 * TODO сделать возможность получения справки по отдельным командам ("help debug", "help game" и т.п.)
 */
@Data
@Component
@EqualsAndHashCode(callSuper = true)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HelpCommand extends Command {

    public void execute(){
        targetChannel.sendMessage(answer.build()).submit();
    }
}