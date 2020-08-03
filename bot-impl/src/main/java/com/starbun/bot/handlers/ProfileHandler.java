package com.starbun.bot.handlers;

import com.starbun.bot.commands.ProfileCommand;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProfileHandler implements CommandHandler<ProfileCommand> {

    @Override
    public ProfileCommand prepare(MessageReceivedEvent message) {
        ProfileCommand profileCommand = new ProfileCommand();
        profileCommand.setTargetChannel(message.getChannel());

        User author = message.getAuthor();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Данные о пользователе " + author.getName());
        eb.setThumbnail(author.getEffectiveAvatarUrl());
        eb.addField("ID", author.getId(), true);
        eb.addField("Tag", author.getAsTag(), true);
        profileCommand.setAnswer(eb);
        return profileCommand;
    }
}
