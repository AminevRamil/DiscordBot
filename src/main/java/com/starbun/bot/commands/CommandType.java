package com.starbun.bot.commands;

import com.starbun.bot.handlers.*;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.List;

/**
 * Перечисление всех возможных типов команд от пользователя
 */
@Getter
public enum CommandType {
    HELP(ImmutableList.<String>builder().add("help", "помощь", "справка").build(), HelpHandler.class),
    ABOUT(ImmutableList.<String>builder().add("about", "описание").build(), AboutHandler.class),
    DEBUG(ImmutableList.<String>builder().add("debug", "дебаг", "отладка").build(), DebugHandler.class),
    PROFILE(ImmutableList.<String>builder().add("profile", "профайл", "профиль").build(), ProfileHandler.class),
    CREATE(ImmutableList.<String>builder().add("create", "создать").build(), CreateHandler.class);

    CommandType(ImmutableList<String> aliases, Class<? extends CommandHandler<? extends Command>> correspondCommandHandler) {
        this.aliases = aliases;
        this.correspondCommandHandler = correspondCommandHandler;
    }

    public List<String> aliases;
    private Class<? extends CommandHandler<? extends Command>> correspondCommandHandler;
}

