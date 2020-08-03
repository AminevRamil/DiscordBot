package com.starbun.bot.handlers;

import com.google.common.collect.ImmutableList;
import com.starbun.bot.listeners.MessageListener;
import com.starbun.bot.commands.DebugCommand;
import com.starbun.bot.exceptions.CommandException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

@Data
@Component
@RequiredArgsConstructor
public class DebugHandler implements CommandHandler<DebugCommand> {

    final private static ImmutableList<String> onAliases = ImmutableList.of("on", "вкл");
    final private static ImmutableList<String> offAliases = ImmutableList.of("off", "выкл");
    final private MessageListener messageListener;

    @Override
    public DebugCommand prepare(MessageReceivedEvent message) {
        String[] args = Arrays.stream(message.getMessage()
                        .getContentRaw()
                        .split(" "))
                .skip(1)
                .map(String::toLowerCase)
                .toArray(String[]::new);

        DebugCommand debugCommand = new DebugCommand(messageListener);
        debugCommand.setTargetChannel(message.getChannel());
        debugCommand.setMode(booleanConverter(args[1]));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Режим дебага", "https://github.com/AminevRamil/StarBunBot");
        if (debugCommand.getMode()) {
            eb.setDescription("Активирован режим отладки");
        } else {
            eb.setDescription("Режим отладки деактивирован");
        }
        eb.setColor(new Color(235, 192, 0));
        eb.setThumbnail("https://avatars1.githubusercontent.com/u/41346424?s=460&u=4ef760d7b73102d55baf43237e33e32c37e3b2b6&v=4");
        debugCommand.setAnswer(eb);
        return debugCommand;
    }

    public static Boolean booleanConverter(String value) {
        if (onAliases.contains(value.toLowerCase())) {
            return true;
        } else if (offAliases.contains(value.toLowerCase())) {
            return false;
        } else {
            throw new CommandException("Неверные аргументы комманды debug");
        }

    }
}
