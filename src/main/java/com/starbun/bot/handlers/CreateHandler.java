package com.starbun.bot.handlers;

import com.google.common.collect.ImmutableList;
import com.starbun.bot.Bot;
import com.starbun.bot.commands.CreateCommand;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Data
@Component
public class CreateHandler implements CommandHandler<CreateCommand> {

    Bot bot;

    @Autowired
    public CreateHandler(Bot bot) {
        this.bot = bot;
    }

    final private static List<String> aliases = ImmutableList.<String>builder().add("канал", "channel").build();

    @Override
    public CreateCommand prepare(MessageReceivedEvent message) {
        String[] args = Arrays.
                stream(message.getMessage()
                        .getContentRaw()
                        .split(" "))
                .skip(1)
                .map(String::toLowerCase)
                .toArray(String[]::new);

        CreateCommand createCommand = new CreateCommand();
        createCommand.setTargetChannel(message.getChannel());
        createCommand.setTargetGuild(message.getGuild());
        createCommand.setTargetName(args[args.length - 1]);
        if (args.length == 4) createCommand.setTargetDist(args[2]);

        makeAnswer(createCommand);

        return createCommand;
    }

    private void makeAnswer(CreateCommand createCommand) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Создание канала");
        eb.setDescription("-----");
        eb.setColor(new Color(235, 192, 0));
        eb.setThumbnail("https://avatars1.githubusercontent.com/u/41346424?s=460&u=4ef760d7b73102d55baf43237e33e32c37e3b2b6&v=4");
        createCommand.setAnswer(eb);
    }
}
