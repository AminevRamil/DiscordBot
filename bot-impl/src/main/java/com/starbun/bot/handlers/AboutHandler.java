package com.starbun.bot.handlers;

import com.starbun.bot.commands.AboutCommand;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.awt.*;

@Data
@Component
public class AboutHandler implements CommandHandler<AboutCommand> {

    @Override
    public AboutCommand prepare(MessageReceivedEvent message) {
        AboutCommand aboutCommand = new AboutCommand();
        aboutCommand.setAnswer(makeAnswer());
        aboutCommand.setTargetChannel(message.getChannel());
        return aboutCommand;
    }

    private EmbedBuilder makeAnswer() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("О боте", "https://github.com/AminevRamil/StarBunBot");
        eb.setDescription("Бот разрабатываемый в ходе ре-тренинга в компании EPAM");
        eb.setColor(new Color(235, 192, 0));
        eb.setThumbnail("https://avatars1.githubusercontent.com/u/41346424?s=460&u=4ef760d7b73102d55baf43237e33e32c37e3b2b6&v=4");
        return eb;
    }
}
