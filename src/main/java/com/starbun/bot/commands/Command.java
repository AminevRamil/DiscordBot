package com.starbun.bot.commands;

import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

@Data
public abstract class Command {
    EmbedBuilder answer;
    MessageChannel targetChannel;

    public abstract void execute();
}
