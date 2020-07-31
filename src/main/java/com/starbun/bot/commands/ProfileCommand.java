package com.starbun.bot.commands;

public class ProfileCommand extends Command {
    @Override
    public void execute() {
        targetChannel.sendMessage(answer.build()).submit();
    }
}
