package com.starbun.bot.listeners;

import com.starbun.bot.exceptions.CommandException;
import com.starbun.bot.util.Filter;
import com.starbun.bot.util.WordFilter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener extends ListenerAdapter {

    final private WordFilter wordFilter;
    private Filter filter;

    @Value("${bot.prefix}")
    private String prefix;

    @Setter
    @Value("${bot.debug}")
    private Boolean debugMode;

    @Value("${bot.id}")
    private long thisBotId;

    //Circular Reference avoid
    @Autowired
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        wordFilter.startupCensoring(event);
    }


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        log.info("Получено сообщение: {}", event.getMessage().toString());
        try {
            String message = event.getMessage().getContentRaw();
            if (filter.isCommand(message)) {
                filter.execute(event);
            } else if (wordFilter.hasBlockWords(message)) {
                event.getMessage().delete().submit();
                event.getChannel().sendMessage("Сообщение пользователя "
                        + event.getAuthor().getAsTag() + " было удалено в связи с цензурой").submit();
            }
        } catch (CommandException e) {
            log.trace("Exception: ", e);
            if (debugMode) {
                event.getChannel().sendMessage(e.eb.build()).submit();
            }
        } catch (Exception e) {
            log.trace("Exception: ", e);
            e.printStackTrace();
            if (debugMode) {
                event.getChannel().sendMessage(String.format("```Java\n%s\n```", e.toString())).submit();
            }
        }
    }
}
