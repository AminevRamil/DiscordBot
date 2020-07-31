package com.starbun.bot.handlers;

import com.starbun.bot.Bot;
import com.starbun.bot.commands.HelpCommand;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;

@Data
@Component
public class HelpHandler implements CommandHandler<HelpCommand> {

    @Value("${bot.prefix}")
    private String prefix;

    @Override
    public HelpCommand prepare(MessageReceivedEvent message) {
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.setAnswer(makeAnswer());
        helpCommand.setTargetChannel(message.getChannel());

        return helpCommand;
    }

    private EmbedBuilder makeAnswer() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Справка по командам бота", "https://github.com/AminevRamil/StarBunBot");
        eb.setDescription("Все команды пишутся с префикса " + prefix +
                " в формате \"" + prefix + " команда [аргументы]\"");
        eb.setColor(new Color(235, 192, 0));
        eb.setThumbnail("https://avatars1.githubusercontent.com/u/41346424?s=460&u=4ef760d7b73102d55baf43237e33e32c37e3b2b6&v=4");
        eb.addField("help", "Отображение данной справки", true);
        eb.addField("about", "Кратное описание бота", true);
        eb.addField("debug [on/off]", "Включение/выключение режима отладки", true);
        eb.addField("profile", "Возвращает данные о пользователе", true);
        return eb;
    }
}
