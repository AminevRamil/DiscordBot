package com.starbun.bot.handlers;

import com.starbun.bot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandHandler<T extends Command> {

    T prepare(MessageReceivedEvent message);
}

