package com.starbun.bot.commands;

import com.starbun.bot.listeners.MessageListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Команда задающая режим дебага в чате.
 */
@Data //?
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@EqualsAndHashCode(callSuper = true)
public class DebugCommand extends Command {

    final private MessageListener messageListener;
    private Boolean mode;


    @Override
    public void execute() {
        targetChannel.sendMessage(answer.build()).submit();
        messageListener.setDebugMode(mode);
    }


}
